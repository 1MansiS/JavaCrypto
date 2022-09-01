# Encrypting/Decrypting Secret Data

These APIs are used when any data needs to be encrypted or decrypted.


## Step 1: Generating Encryption Parameters

Endpoint: 

```plaintext
GET /generate-encryption-parameters/{enc_algo}
```

Sample Request:

```plaintext
curl 'http://localhost:8080/generate-encryption-parameters' 
```

OR

```plaintext
curl 'http://localhost:8080/generate-encryption-parameters/chacha20'
```


|Name|Type|Description|
|---|---|---|
|`enc_algo`<BR>(Optional)|String|Specify the encryption algorithm being used for encryption/decryption process. Key and Initialization parameters would be constructed accordingly. **Valid values:** ChaCha20, chacha or AES. Defaults to `AES`.


Response:

```plaintext
{
   "base64_iv" : "F6VxNXu/DgeFSJN8pAzWYw==",
   "base64_symmetric_key" : "i/vzG5meue8XOVecwyFwDmuwvnRDUIBU5MHIrlusrFs="
}
```

|Name|Type|Description|
|---|---|---|
|`base64_symmetric_key`|String|Based on the algorithm choosen, 256 bits csprng symmetric key. Its Base64 encoded for transmiting purposes |
|`base64_iv`|String|AES needs 16 bytes Initialization vector and ChaCha20 needs 12 bytes nonces. Based on the algorithm choosen, corresponding unique, csprng initialization vector. It is base64 encoded for transmiting purposes|



## Step 2A: Encryption Operation

```plaintext
POST /encrypt
```

Sample Request

```plaintext
curl 'http://localhost:8080/encrypt' -X POST -H "Content-Type: application/json" -d \
'{"base64_symmetric_key" : "i/vzG5meue8XOVecwyFwDmuwvnRDUIBU5MHIrlusrFs=",
"base64_iv" : "F6VxNXu/DgeFSJN8pAzWYw==",
"plain_text":"Hello Crypto World!",
"aad":"localhost"}' -s | json_pp
```

|Name|Type|Description|
|---|---|---|
|`base64_symmetric_key`<BR>(Required)|String|Base64 encoded symmetric key from above operation|
|`base64_iv`<BR>(Required)|String|Base64 encoded initialization vector from above operation|
|`plain_text`<BR>(Required)|String|Secret plain text message to be encrypted|
|`aad`<BR>(Required only for AES)|String|Authentication Tag of atleast 128 bits in length. Common practice is origin location.|

Response:

```plaintext
{
   "aad" : "localhost",
   "base64_cipher_text" : "7MHFX0GCarLb9/xporC38Ft98ZHMMZnFxqSQfazRNSFEg1I=",
   "base64_iv" : "F6VxNXu/DgeFSJN8pAzWYw==",
   "base64_symmetric_key" : "i/vzG5meue8XOVecwyFwDmuwvnRDUIBU5MHIrlusrFs=",
   "plain_text" : "Hello Crypto World!"
}
```

|Name|Type|Description|
|---|---|---|
|`base64_symmetric_key`|String|Base64 encoded symmetric key from above operation|
|`base64_iv`|String|Base64 encoded initialization vector from above operation|
|`plain_text`|String|Secret plain text message to be encrypted|
|`base64_cipher_text`|String|Corresponding Cipher text, base64 encoded for transmission|
|`aad`|String|Authentication Tag of atleast 128 bits in length. Common practice is origin location.|


## Step 2B: Decryption Operation:

```plaintext
POST /decrypt
```

Sample Request

```plaintext
curl 'http://localhost:8080/decrypt' -X POST -H "Content-Type: application/json" \
-d '{"base64_symmetric_key":"i/vzG5meue8XOVecwyFwDmuwvnRDUIBU5MHIrlusrFs=",
"base64_iv":"F6VxNXu/DgeFSJN8pAzWYw==",
"aad":"localhost",
"base64_cipher_text":"7MHFX0GCarLb9/xporC38Ft98ZHMMZnFxqSQfazRNSFEg1I="}'
```

|Name|Type|Description|
|---|---|---|
|`base64_symmetric_key`<BR>(Required)|String|Base64 encoded symmetric key from above operation|
|`base64_iv`<BR>(Required)|String|Base64 encoded initialization vector from above operation|
|`base64_cipher_text`<BR>(Required)|String|Base64 encoded cipher text|
|`aad`<BR>(Required only for AES)|String|Authentication Tag of atleast 128 bits in length. Common  practice is origin location.|

Response:

```plaintext
{
   "aad" : "localhost",
   "base64_cipher_text" : "7MHFX0GCarLb9/xporC38Ft98ZHMMZnFxqSQfazRNSFEg1I=",
   "base64_iv" : "F6VxNXu/DgeFSJN8pAzWYw==",
   "base64_symmetric_key" : "i/vzG5meue8XOVecwyFwDmuwvnRDUIBU5MHIrlusrFs=",
   "plain_text" : "Hello Crypto World!"
}
```

|Name|Type|Description|
|---|---|---|
|`base64_symmetric_key`|String|Base64 encoded symmetric key from above operation|
|`base64_iv`|String|Base64 encoded initialization vector from above operation|
|`base64_cipher_text`|String|Base64 encoded cipher text|
|`plaintext`|String|Original Plain Text|
|`aad`|String|Authentication Tag of atleast 128 bits in length. Common  practice is origin location.|