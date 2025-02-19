package com.bravos2k5.bravosshop.common.utils;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.logging.Logger;

public class KeyLoader {

    private static final Logger logger = Logger.getLogger(KeyLoader.class.getName());

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static PrivateKey loadPrivateKey(String filePath) {
        try (FileReader fileReader = new FileReader(filePath)) {
            PEMParser pemParser = new PEMParser(fileReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            Object object = pemParser.readObject();
            switch (object) {
                case PEMKeyPair pemKeyPair -> {
                    PrivateKeyInfo privateKeyInfo = pemKeyPair.getPrivateKeyInfo();
                    logger.info("PEM Private non-encrypted key loaded");
                    return converter.getPrivateKey(privateKeyInfo);
                }
                case PrivateKeyInfo privateKeyInfo -> {
                    logger.info("Private key info loaded");
                    return converter.getPrivateKey(privateKeyInfo);
                }
                default -> throw new RuntimeException("Unknown object type: " + object.getClass().getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey loadPrivateKey(String filePath, String password) {
        try (FileReader fileReader = new FileReader(filePath)) {
            PEMParser pemParser = new PEMParser(fileReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            Object object = pemParser.readObject();
            switch (object) {
                case PKCS8EncryptedPrivateKeyInfo pkcs8EncryptedPrivateKeyInfo -> {
                    InputDecryptorProvider provider = new JceOpenSSLPKCS8DecryptorProviderBuilder()
                            .setProvider("BC")
                            .build(password.toCharArray());
                    logger.info("PKCS8 Private key loaded");
                    return converter.getPrivateKey(pkcs8EncryptedPrivateKeyInfo.decryptPrivateKeyInfo(provider));
                }
                case PEMEncryptedKeyPair pemEncryptedKeyPair -> {
                    PEMDecryptorProvider provider = new JcePEMDecryptorProviderBuilder()
                            .setProvider("BC")
                            .build(password.toCharArray());
                    PEMKeyPair keyPair = pemEncryptedKeyPair.decryptKeyPair(provider);
                    logger.info("PEM Private encrypted key loaded");
                    return converter.getPrivateKey(keyPair.getPrivateKeyInfo());
                }
                default -> throw new RuntimeException("Unknown object type: " + object.getClass().getName());
            }
        } catch (IOException | OperatorCreationException | PKCSException e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey loadPublicKey(String filename) {
        try (FileReader fileReader = new FileReader(filename)) {
            PEMParser pemParser = new PEMParser(fileReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            Object object = pemParser.readObject();
            return converter.getPublicKey(SubjectPublicKeyInfo.getInstance(object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
