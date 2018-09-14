Above code base, has a Java Module (JavaCryptoModule), which exposes apis, responsible for performing various cryptographic primitives in a most secure way possible thru JCA. 

This module has support for performing:

* symmetric encryption/decryption, 
* computing message digests, 
* sender and receiver components of Message Authentication Code (MAC)
* Signing and Verifying Digital Signatures
* Password Storage

`SecureCryptoMicroservice` showcases, in most simplistic way on how to use JavaCryptoModule. It should be thought as any kind of service for e.g. lambda, or a microservice etc which would need some cryptographic work.

Docker image can be downloaded from [docker.hub](https://hub.docker.com/r/1mansis/javacrypto/), which should build and setup all above for anyone who wishes to experiment with this. 

**Docker setup:**

```
docker pull docker pull 1mansis/javacrypto
docker run 1mansis/jaavcrypto -p 8080:8080
```

Once you have the image going, follow corresponding sections below on how to use various micro service endpoints:

**Encryption/Decryption:**

```
bash-3.2$ curl -X POST 'http://localhost:8080/encrypt' --data-urlencode "plain_text=Hello World of Cryptography with Java 10"
9A0eHSNmqnug2kD+ej/c/HQLceuSz6hZ8DYLVabyV7+VN1zl/FhQeJ3+HXnD7D4Jsj7z6FEvcl8=
bash-3.2$ curl -X POST 'http://localhost:8080/decrypt' --data-urlencode "cipher_text=9A0eHSNmqnug2kD+ej/c/HQLceuSz6hZ8DYLVabyV7+VN1zl/FhQeJ3+HXnD7D4Jsj7z6FEvcl8="
Hello World of Cryptography with Java 10
```

**Note:** Cryptographic keying material is being persisted on file system. Thus, decryption should immediately follow encryption, to be successful.

**Message Digest:**

```
bash-3.2$ curl -X POST 'http://localhost:8080/message_digest' --data-urlencode "data=Hello World of Cryptography with Java 10"
56zhjuEo8+3krBDAqneHGtMcMg1tmgbuQH7GQGLM54O5of3mYWWKZeZVT+gAAtpZr4Cxl6S9NFhveFKi6+GnVA==
```

**Message Authentication Code:**

```
bash-3.2$ curl -X POST 'http://localhost:8080/mac_sender' --data-urlencode "data=Hello World of Cryptography with Java 10"
QVr/R6JRkvU7oRR+plJOQscLXxRoJB/KtfpxaY+6gYM=
bash-3.2$ curl -X POST 'http://localhost:8080/mac_receiver' --data-urlencode "data=Hello World of Cryptography with Java 10" --data-urlencode "mac=QVr/R6JRkvU7oRR+plJOQscLXxRoJB/KtfpxaY+6gYM="
true
bash-3.2$ 
```

**Digital Signature:**

```
bash-3.2$ curl -X POST 'http://localhost:8080/digital_signature_sign' --data-urlencode "data=Hello World of Cryptography with Java 10"
AcnfAAOj2muJHCTmWphU8dI0noHt+IaRqHHt0yFPneH0496gzIIqANfs7T3si9oF9SapwPAzsvNrig5eGZKQr5+djBOdzUquufDpPlWG7P60sQ7d7kWXqBZKpg3snyAe8YAe/1ho+xDlaD5tB1Ke2x7DWqyDGCLKU0OtmnsFbUFGQkWE1GtZPBH3GljSnkcHegrx3BEUh9w/m3Lz12+ZZI0oF7Rtcb4eaOV6NmPOCA5FHEMVsFXCgJoRmqNB3PGX30ufg5or6Z6J0AZhqxJBRKJKPMMGS8wXM43vKLzrWzJPF3pE+ptiQ50BOBOPPgz52GKDyKC5sFhZdMXe2L3xOCEPo9IRc454JsgWn52CSwg0HxVl7nZH4+u0wCWNKhuWJX5VLiY4rgc4A4NqWQk/M9ozRXntyjdEwm+CFctWe+B6br/v1jl/cwpmTZJCKNa0kRvzS5oVQq1GHh3b0R6bE0K4S9dHotJ5NAeEZQLfp/kG2gfS78VYO6/yfJoDF/gg+/DIpRetLB6X7tQeCNXRMDTiUEU6V0uUjKitYLjZGbCy3HKFaN9adDM3v0joTT89I/zHNuX+0M37FfdFJffePTZpFQ2mB6MeFj0+E2034acopm5zayt5hLk92oAyte+ivFmmb7bUfJVKoGaBIOuGT7GvuV6E1ateBOB2qZ5QKR0=
bash-3.2$ curl -X POST 'http://localhost:8080/digital_signature_verify' --data-urlencode "data=Hello World of Cryptography with Java 10" --data-urlencode "sign=AcnfAAOj2muJHCTmWphU8dI0noHt+IaRqHHt0yFPneH0496gzIIqANfs7T3si9oF9SapwPAzsvNrig5eGZKQr5+djBOdzUquufDpPlWG7P60sQ7d7kWXqBZKpg3snyAe8YAe/1ho+xDlaD5tB1Ke2x7DWqyDGCLKU0OtmnsFbUFGQkWE1GtZPBH3GljSnkcHegrx3BEUh9w/m3Lz12+ZZI0oF7Rtcb4eaOV6NmPOCA5FHEMVsFXCgJoRmqNB3PGX30ufg5or6Z6J0AZhqxJBRKJKPMMGS8wXM43vKLzrWzJPF3pE+ptiQ50BOBOPPgz52GKDyKC5sFhZdMXe2L3xOCEPo9IRc454JsgWn52CSwg0HxVl7nZH4+u0wCWNKhuWJX5VLiY4rgc4A4NqWQk/M9ozRXntyjdEwm+CFctWe+B6br/v1jl/cwpmTZJCKNa0kRvzS5oVQq1GHh3b0R6bE0K4S9dHotJ5NAeEZQLfp/kG2gfS78VYO6/yfJoDF/gg+/DIpRetLB6X7tQeCNXRMDTiUEU6V0uUjKitYLjZGbCy3HKFaN9adDM3v0joTT89I/zHNuX+0M37FfdFJffePTZpFQ2mB6MeFj0+E2034acopm5zayt5hLk92oAyte+ivFmmb7bUfJVKoGaBIOuGT7GvuV6E1ateBOB2qZ5QKR0="
true
bash-3.2$ 

```


# Security Architecture Must-dos
* A matured Key Management System (for e.g. AWS KMS, vault, Safenet etc), should be used for any kind of key material management (like encryption keys, initialization vectors etc) management. Just to keep above microservice, as a simple demo on how to use Java crypto module, I have taken various shortcuts. One of them being persisting cyptographic materials. This should **NOT** be done in real world applications. 
* Every effort is being taken to keep above microservice stateless. This mitigates  complicated 2 way ssl, and certificate management. But, make sure all communication is happening over https.

# Slides Changes:
	Slide 6: Add NaCl/Google Tink
	
# References:
1. https://security.googleblog.com/2018/08/introducing-tink-cryptographic-software.html
