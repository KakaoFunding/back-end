package org.kakaoshare.backend.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    public static final String PBE_WITH_MD_5_AND_DES = "PBEWithMD5AndDES";
    public static final String BASE_64 = "base64";
    public static final String ITERATIONS = "1000";
    public static final String RANDOM_SALT_GENERATOR = "org.jasypt.salt.RandomSaltGenerator";
    public static final String POOL_SIZE = "1";

    @Value("${jasypt.encryptor.password}")
    private String encryptKey;
    
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(encryptKey);
        config.setPoolSize(POOL_SIZE);
        config.setAlgorithm(PBE_WITH_MD_5_AND_DES);
        config.setStringOutputType(BASE_64);
        config.setKeyObtentionIterations(ITERATIONS);
        config.setSaltGeneratorClassName(RANDOM_SALT_GENERATOR);
        encryptor.setConfig(config);
        return encryptor;
    }
}