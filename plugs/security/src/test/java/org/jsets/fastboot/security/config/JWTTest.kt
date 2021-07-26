package org.jsets.fastboot.security.config

import io.jsonwebtoken.*
import org.jsets.fastboot.common.util.StringUtils
import org.junit.jupiter.api.Test
import javax.xml.bind.DatatypeConverter

class JWTTest {

    @Test
    fun case1(){
        val signKey = "testtttt"
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(signKey)
        val builder = Jwts.builder()
        builder.setId(StringUtils.getUUID())
        builder.setSubject("wj")
        builder.compressWith(CompressionCodecs.DEFLATE)
        builder.signWith(SignatureAlgorithm.HS256, secretKeyBytes)
        val JWT =  builder.compact();

        val claims = Jwts.parser()
                .setSigningKey(secretKeyBytes)
                .parseClaimsJws(JWT)
                .body

        println(claims.expiration)
    }

}