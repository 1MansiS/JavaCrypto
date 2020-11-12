Above code base, has a Java Module (`JavaCryptoModule`), which exposes apis, responsible for performing various cryptographic primitives in a most secure way possible thru JCA. 

This module has support for performing:

* symmetric encryption/decryption, 
* computing message digests, 
* sender and receiver components of Message Authentication Code (MAC)
* Signing and Verifying Digital Signatures
* Password Storage

`SecureCryptoMicroservice` showcases, in most simplistic way on how to use JavaCryptoModule. It should be thought as any kind of service for e.g. lambda, monolithic application, microservice etc which would need some cryptographic work.

Docker image can be downloaded from [docker.hub](https://hub.docker.com/r/1mansis/javacrypto/), which should build and setup all above for anyone who wishes to experiment with this. 

# Docker setup:

```
docker pull 1mansis/javacrypto
docker run -p 8080:8080 1mansis/javacrypto
```

Once you have the image going, follow corresponding sections below on how to use various micro service endpoints:

## Encryption/Decryption:

```
Step 1: Generating Symmetric Key:
Request: curl 'http://localhost:8080/generateSymmetricKey' -X POST -H "Content-Type: application/json" -d '{"key_size":"256","enc_algo":"AES"}'
Response: 
{
  "enc_algo" : "AES",
  "symmetric_key" : "0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y=",
  "key_size" : 256
}

Step 2: Generating Initialization Vector:
Request: curl 'http://localhost:8080/generateInitializationVector' -X POST -H "Content-Type: application/json" -d '{"iv_size":"16"}'

Response: 
{
  "iv_size" : 16,
  "IV" : "qVsGLYhOnzBbDUIyTk595w=="
}

Step 3: Encryption
Request: curl 'http://localhost:8080/encrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y=","IV":"qVsGLYhOnzBbDUIyTk595w==","plain_text":"Hello Crypto World!","aad":"localhost","enc_algo":"AES"}'

Response: 

{
  "plain_text" : "Hello Crypto World!",
  "base64_cipher_text" : "OvnpZsO0gzfZ+yRCugFWLSgl5MZmj4VZNX8tf00jCViWk4o=",
  "symmetric_key" : "0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y=",
  "aad" : "localhost",
  "IV" : "qVsGLYhOnzBbDUIyTk595w==",
  "enc_algo" : "AES"
}

Step 4: Decryption
Request: curl 'http://localhost:8080/decrypt' -X POST -H "Content-Type: application/json" -d '{"symmetric_key":"0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y=","IV":"qVsGLYhOnzBbDUIyTk595w==","base64_cipher_text":"OvnpZsO0gzfZ+yRCugFWLSgl5MZmj4VZNX8tf00jCViWk4o=","enc_algo":"AES","aad":"localhost"}'

Response:

{
  "plain_text" : "Hello Crypto World!",
  "enc_algo" : "AES",
  "IV" : "qVsGLYhOnzBbDUIyTk595w==",
  "base64_cipher_text" : "OvnpZsO0gzfZ+yRCugFWLSgl5MZmj4VZNX8tf00jCViWk4o=",
  "aad" : "localhost",
  "symmetric_key" : "0pbH8pcnl51eAlUTLgcGCbR1FKFkBsLIFJ1kgAmne6Y="
}
```


## Message Digest:

```
Request:  curl 'http://localhost:8080/digest' -X POST -H "Content-type: application/json" -d '{"message":"Hello Hashing!!!"}' | json_pp

Response: 
{
   "hash" : "Mb9x21/z6XCh3OiwzWSfkxnybuKPRe0FiSqxLDkNDGRPcRzcvEHUrSRF6iseByz/qVtgXc3qYe4U1gWZkM2B7A==",
   "message" : "Hello Hashing!!!"
}
```

## Message Authentication Code:


## Digital Signature:

```
Step 1: Generating Keys based on edward curves or NIST curves:
Request: curl -X POST 'http://localhost:8080/generateAsymmetricKey' -H  "Content-Type: application/json" -d '{"asymm_algo":"ed-curve"}' | json_pp

Response: 
{
 "asymm_algo" : "ed-curve",
 "base64_public_key" : "MCowBQYDK2VwAyEAW5CKDO5xEO5EVVIcMeBFJ0w4nI6MDmWjWEHgZ4Zqeoc=",
 "base64_private_key" : "MC4CAQAwBQYDK2VwBCIEIJtmYm8YaxQW50/LI8Uhf3ft1uUB7kKHbK7jF0Ze1SqF"
}

Step 2: Signing a message; generating Signature for plain text message
Request: curl -X POST 'http://localhost:8080/sign' -H  "Content-Type: application/json" -d '{"asymm_algo":"ed-curve","base64_private_key":"MC4CAQAwBQYDK2VwBCIEIJtmYm8YaxQW50/LI8Uhf3ft1uUB7kKHbK7jF0Ze1SqF","plaintext_message":"Hello World!"}' | json_pp

Response: 
{
  "plaintext_message" : "Hello World!",
  "asymm_algo" : "ed-curve",
  "base64_sign" : "FOh5uarkS3MMdkraAUJywSK8M/SXQbgbOjjze0zgsDzQ0QqH3++dbeev/WPdKdKXQwRDzY0v8rUKP1rDAL0MBQ==",
  "base64_private_key" : "MC4CAQAwBQYDK2VwBCIEIJtmYm8YaxQW50/LI8Uhf3ft1uUB7kKHbK7jF0Ze1SqF"
}

Step 3: Verifying a message
Request: curl -X POST 'http://localhost:8080/verify' -H  "Content-Type: application/json" -d '{"asymm_algo":"ed-curve","plaintext_message":"Hello World!","base64_public_key":"MCowBQYDK2VwAyEAW5CKDO5xEO5EVVIcMeBFJ0w4nI6MDmWjWEHgZ4Zqeoc=","base64_sign":"FOh5uarkS3MMdkraAUJywSK8M/SXQbgbOjjze0zgsDzQ0QqH3++dbeev/WPdKdKXQwRDzY0v8rUKP1rDAL0MBQ=="}' | json_pp

Response: 
{
  "base64_sign" : "FOh5uarkS3MMdkraAUJywSK8M/SXQbgbOjjze0zgsDzQ0QqH3++dbeev/WPdKdKXQwRDzY0v8rUKP1rDAL0MBQ==",
  "plaintext_message" : "Hello World!",
  "asymm_algo" : "ed-curve",
  "verified" : true,
  "base64_public_key" : "MCowBQYDK2VwAyEAW5CKDO5xEO5EVVIcMeBFJ0w4nI6MDmWjWEHgZ4Zqeoc="
}

```

## Secure Password Storage and Authentication

```
bash-3.2$ curl 'http://localhost:8080/calc_passwd_storage' --data-urlencode "plain_text_password=Passw0rd"
yrEjH50LrXAAqaB3Ky35DV0g8E1f2nXO40qEQfc/QA6OTexyIHJZNoLOJxjEozMnUrv3Ja/j7/OJyo+r77kK2vgrGuyJ1sRMP9C/iRd2hjfbJZ05cBYT8v514lv0RWODTcr1pdBvXG0qPl/f+W82Z9z13Zm+KWxk11e84pRsrlF0EnxAtIChZhzy0zWb4L3c7N9cB41NQ7MozRia75TKxbwsLTnO2i74cdGUsA3WvLyYDs0aCxxKDEisc9GJW4vD7xs/tVv+0SMOvK1B6LE6DA2D+VraDcH638Fb1gYxYLBBatjBjHDJgo8FOkEETS7LVQ0APwWh5j9t2AFQ9Hfmyw==msheth-mbp:SecureCryptoMicroservice msheth$ curl 'http://localhost:808rm public.key 
bash-3.2$ curl 'http://localhost:8080/authenticate' --data-urlencode "plain_text_password=Passw0rd"
true
```

# Security Architecture Must-dos
* A matured Key Management System (for e.g. AWS KMS, vault, Safenet etc), should be used for any kind of key material management (like encryption keys, initialization vectors etc) management. Just to keep above microservice, as a simple demo on how to use Java crypto module, I have taken various shortcuts. One of them being persisting cyptographic materials. This should **NOT** be done in real world applications. 
* Every effort is being taken to keep above microservice stateless. This mitigates  complicated 2 way ssl, and certificate management. But, make sure all communication is happening over https.

	
# References:
1. [Google Tink Cryptographic Software](https://security.googleblog.com/2018/08/introducing-tink-cryptographic-software.html)
2. [Bouncy Castle Provider Installation](http://www.bouncycastle.org/wiki/display/JA1/Provider+Installation)
