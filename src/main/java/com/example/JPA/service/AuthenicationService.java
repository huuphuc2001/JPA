package com.example.JPA.service;

import com.example.JPA.dto.request.AuthenicationRequest;
import com.example.JPA.dto.request.LogoutRequest;
import com.example.JPA.dto.request.VerifyTokenRequest;
import com.example.JPA.dto.respone.AuthenicationRespone;
import com.example.JPA.dto.respone.VerifyTokenRespone;
import com.example.JPA.entity.LogoutToken;
import com.example.JPA.entity.User;
import com.example.JPA.exception.AppException;
import com.example.JPA.exception.ErrorCode12;
import com.example.JPA.repository.LogoutTokenRepository;
import com.example.JPA.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
//import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.text.ParseException;
import java.time.Instant;

import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

//class này để kiểm tra pass word của user nhập vào vs passwword của user trong data có match vs nhauu ko
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthenicationService {
    UserRepository repository;
    LogoutTokenRepository logoutTokenRepository;
    @NonFinal //đanh dấu này để ko inject vào constructer
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
//            = "EBCIEEitKzA3lLqTWHT3zLr0e3M2ZDZeo0l4VtMJXuFP9716dK/YNmDjJkam/yu4";

    public VerifyTokenRespone verifyTokenRespone(VerifyTokenRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean invalid = true;
        try {
            verifyToken(token);
        }catch (AppException e) {
            invalid = false;
        }
        return VerifyTokenRespone.builder()
//                .valid(verified && exp.after(new Date()))//tg hết hạn phải sau tg hiện tại
                .valid(invalid)
                .build();
    }

    //login thành công sẽ trả về token
    public AuthenicationRespone authenicated(AuthenicationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode12.USER_NOT_EXISTED));

        boolean authenicated = passwordEncoder.matches(request.getPassword(),user.getPassword());
        if(!authenicated){
            throw new AppException(ErrorCode12.UNAUTHENICATED);
        }
        var token = generateToken(user);

        return AuthenicationRespone.builder()
                .token(token)
                .authenicated(true)
                .build();
    }

    //tạo token thu vien nimbusds
    private String generateToken(User user){
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 3600 * 1000);
        //header - giai đoạn 1
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //payload - giai đoạn 2
        //data trong body của payload gọi là claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())//user đăng nhập
                .issuer("example.com")//xác định token dc issue từ ai
                .issueTime(now)//lấy tg hiện tại
                .expirationTime(expirationTime)//set thời gian tồn tại
                .claim("scope", buildscope(user))//thêm 1 claim - thêm vào để làm ví dụ
                .jwtID(UUID.randomUUID().toString())//lưu token của user đã logout cho nhẹ
                //uuid: chuổi 32 ký tự dc random ngẫu nhiên và không trùng kể cả trên toàn thế giới
                //b2: làm entity lưu lại token đã dc logout
                .build();
        //payload cần body là           claim
        //convert claimset về kiểu json => payload sẽ nhận 1 json obj
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        //jwsobject yêu cầu 2 param header, payload
        //header để jws xác định thuật toán mã hoá ở đây là gì
        JWSObject jwsObject = new JWSObject(header,payload);

        //signature "ký token" - giai đoạn 3
        //MACSigner thuật toán ký
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    //logout
    //thêm token hiện tại đã hết hạn vào entity logoutToken
    //sau 1 ngày sẽ dùng hàm để xoá nó đi
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        //đọc thông tin của token để lấy ra id,exp(tg hết hạn)
        var signToken = verifyToken(request.getToken());
        //claimset tức là các claim trong payload
        String jti = signToken.getJWTClaimsSet().getJWTID();//token id
        Date expTime = signToken.getJWTClaimsSet().getExpirationTime();//exp time
        LogoutToken logoutToken = LogoutToken.builder()
                .id(jti)
                .expiryTime(expTime)
                .build();
        logoutTokenRepository.save(logoutToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {


        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        //kt token đã hết hạn chưa
        Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);//trả về true|false
        //nếu chữ ký ko đúng(var verified = false)
        // hoặc token đã hết hạn cũng sẽ throw ra error
        if(!verified && exp.after(new Date())){
            throw new AppException(ErrorCode12.UNAUTHENICATED);
        }
        //để xác nhận token hết hạn sẽ trả về false
        if (logoutTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode12.UNAUTHENICATED);
        return signedJWT;//trả về thông tin chi tiết của token để sử dụng cho việc logout

    }

    //build scope của user
    private String buildscope(User user){
        //scope trong oauth2 phân cách nhau bằng "dấu cách"
        //VD trong jwt.io: "scope": "USER ADMIN"
        StringJoiner stringJoiner = new StringJoiner(" ");
        //CollectionUtils.isEmpty() kt xem 1 collection có rỗng(null) ko
        //nếu ko null nó sẽ kiểm tra xem collection đó có phần tử nào không.
        //isNull > true | not null > false
        //trong đây thì kt xem role user có null ko
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                //add role vào scope
//                stringJoiner.add("ROLE_" + role.getName()); *****
                stringJoiner.add(role.getName());
//                if(!CollectionUtils.isEmpty(role.getPermissions()))
//                role.getPermissions()
//                        .forEach(permission -> stringJoiner.add(permission.getName()));
            });
        return stringJoiner.toString();
    }
}
