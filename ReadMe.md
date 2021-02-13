Above code base, has a Java Module (`JavaCryptoModule`), which exposes apis, responsible for performing various cryptographic primitives in a most secure way possible thru JCA. 

This module has support for performing:

* symmetric encryption/decryption using AES-GCM and ChaCha20-Poly1305 crypto schemes, 
* computing message digests, 
* Computing Message Authentication Code
* Signing and Verifying Digital Signatures using edward curves or EDDSA mechanism using NIST curves
* Computing hashes using Key Derivation function PBKDF2

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

## Message Authentication Code (MAC):

```
Step 1: Generate symmetric key for computing MAC
Request: curl 'http://localhost:8080/compute-hmac-key' -X POST -H "Content-type: application/json" | json_pp

Response:
{
   "base64-symmetric-key" : "CwyXz3ZymqD3eKzlo3xwmloL5WjEDAyF2iVC4L4xoHk="
}

Step 2: Compute MAC
Request: curl 'http://localhost:8080/compute-mac' -X POST -H "Content-type: application/json" -d '{"message":"Hello MAC!!!","base64-symmetric-key":"CwyXz3ZymqD3eKzlo3xwmloL5WjEDAyF2iVC4L4xoHk="}' | json_pp

Response: 

{
   "message" : "Hello MAC!!!",
   "base64-symmetric-key" : "CwyXz3ZymqD3eKzlo3xwmloL5WjEDAyF2iVC4L4xoHk=",
   "mac" : "OWEjsI5RguL47XBjUPGvfla5z66P3lSt6puYpPUeosa7AFCU4+SDAWX4VrZ+xuukHOqoHS1smE8Kiixrut6BHA=="
}


```


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
Compute Password using Argon2
----------------------------
Step 1: Generate salt of size 256 bits
Request: curl 'http://localhost:8080//compute-salt' -X POST -H "Content-type: application/json" -d '{"salt-size":"32"}' | json_pp

Response:
{
   "base64-salt" : "6hCkSssrQnRV5DIeMSE+0kPV83dFSTl/Mnf7C7Nf8Ng=",
   "salt-size" : "32"
}

Step 2: Compute Hash, using above generated unique, csprng salt
Request: curl 'http://localhost:8080/compute-kdf-passwd' -X POST -H "Content-type: application/json" -d '{"base64-salt":"6hCkSssrQnRV5DIeMSE+0kPV83dFSTl/Mnf7C7Nf8Ng=","passwd":"mysupersecretpasswordtobestored!!!","kdf-algo":"argon2"}' | json_pp

Response:
{
   "passwd" : "mysupersecretpasswordtobestored!!!",
   "kdf-algo" : "argon2",
   "base64-kdf-passwd-hash" : "7XwIqJB+uLU+L3+2TQEyNttYY7sxmb09N9drS09MMgQ=",
   "base64-salt" : "6hCkSssrQnRV5DIeMSE+0kPV83dFSTl/Mnf7C7Nf8Ng="
}

Compute Password Using PBKDF2
-----------------------------

Step 1: Generate salt of size 256 bits
Request: curl 'http://localhost:8080//compute-salt' -X POST -H "Content-type: application/json" -d '{"salt-size":"32"}' | json_pp

Response: 
{
   "salt-size" : "32",
   "base64-salt" : "hgb7F7/GhZWyfXSxEDHs2yxSbkD/jnpzBDBIXcs9Hzo="
}

Step 2: Compute Hash, using above generated unique, csprng salt
Request: curl 'http://localhost:8080/compute-kdf-passwd' -X POST -H "Content-type: application/json" -d '{"base64-salt":"hgb7F7/GhZWyfXSxEDHs2yxSbkD/jnpzBDBIXcs9Hzo=","passwd":"mysupersecretpasswordtobestored!!!","kdf-algo":"pbkdf2"}' | json_pp

Response: 
{
   "passwd" : "mysupersecretpasswordtobestored!!!",
   "base64-salt" : "hgb7F7/GhZWyfXSxEDHs2yxSbkD/jnpzBDBIXcs9Hzo=",
   "base64-kdf-passwd-hash" : "9lwl5Bue+uThp20QdfG88KKBhH5OAEv5ntH/ILVid7U=",
   "kdf-algo" : "pbkdf2"
}


Compute Password Using bcrypt 
-----------------------------

Step 1: Generate salt of size 128 bits
Request: curl 'http://localhost:8080//compute-salt' -X POST -H "Content-type: application/json" -d '{"salt-size":"16"}' | json_pp

Response:
{
   "salt-size" : "16",
   "base64-salt" : "b/D8O/B5BFrTkce1jzxZDw=="
}

Step 2: Compute Hash, using above generated unique, csprng salt
Request: curl 'http://localhost:8080/compute-kdf-passwd' -X POST -H "Content-type: application/json" -d '{"base64-salt":"b/D8O/B5BFrTkce1jzxZDw==","passwd":"mysupersecretpasswordtobestored!!!","kdf-algo":"bcrypt"}' | json_pp

Response: 
{
   "base64-salt" : "b/D8O/B5BFrTkce1jzxZDw==",
   "kdf-algo" : "bcrypt",
   "passwd" : "mysupersecretpasswordtobestored!!!",
   "base64-kdf-passwd-hash" : "KM7Cq3GdjrWqYr/NZpqcXnXcu/Ksvuwr"
}


Compute Password Using scrypt 
-----------------------------

Step 1: Generate salt of size 256 bits
Request: curl 'http://localhost:8080//compute-salt' -X POST -H "Content-type: application/json" -d '{"salt-size":"32"}' | json_pp 

Response:
{
   "salt-size" : "32",
   "base64-salt" : "k87h8D0cRV80Tu0ZhN3PBvgJ/ldLTtvvZBCuJYvOhFc="
}


Step 2: Compute Hash, using above generated unique, csprng salt
Request: curl 'http://localhost:8080/compute-kdf-passwd' -X POST -H "Content-type: application/json" -d '{"base64-salt":"k87h8D0cRV80Tu0ZhN3PBvgJ/ldLTtvvZBCuJYvOhFc=","passwd":"mysupersecretpasswordtobestored!!!","kdf-algo":"scrypt"}' | json_pp

Response:
{
   "kdf-algo" : "scrypt",
   "base64-kdf-passwd-hash" : "ZkirenAEqtAoH0xh3A7dw7yUn5yglzxb2kbha4N1PoE=",
   "passwd" : "mysupersecretpasswordtobestored!!!",
   "base64-salt" : "k87h8D0cRV80Tu0ZhN3PBvgJ/ldLTtvvZBCuJYvOhFc="
}

```

# Security Architecture Must-dos
* A matured Key Management System (for e.g. AWS KMS, vault, Safenet etc), should be used for any kind of key material management (like encryption keys, initialization vectors, salts, password hashes etc) management. Just to keep above microservice, as a simple demo on how to use Java crypto module, I have taken various shortcuts. 
* Every effort is being taken to keep above microservice stateless. This mitigates  complicated 2 way ssl, and certificate management. But, make sure all communication is happening over https.

	
