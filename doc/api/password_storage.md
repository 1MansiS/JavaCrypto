# Generating Hashed Password using Key Derivative Functions (KDF) for Secure Storage:

## Compute salt

Endpoint:

```plaintext
GET /compute-salt/{kdf_algo}
```

Sample Request:

```plaintext
curl 'http://localhost:8080/compute-salt' -s | json_pp
```

OR

```plaintext
curl 'http://localhost:8080/compute-salt/pbkdf2'
```

|Name|Type|Description|
|---|---|---|
|`kdf-algo`<BR>(Optional)|String|KDF Algorithm to be used to compute the password hash for storage. **Valid Values:**: pbkdf, bcrypt, scrypt, argon2. Default Argon2.|


Response:

```plaintext
{
   "base64-salt" : "I3+v64AxbXK9qVrvOV04H/fAHy5q/InjZe0791RzS5k="
}
```

|Name|Type|Description|
|---|---|---|
|`base64-salt`|String|Salt to use to compute password hash for storage|


## Compute KDFed Password

```plaintext
POST /compute-kdf-passwd
```

Sample Request:

```plaintext
curl 'http://localhost:8080/compute-kdf-passwd' -X POST -H "Content-type: application/json"  \
-d '{"base64-salt":"I3+v64AxbXK9qVrvOV04H/fAHy5q/InjZe0791RzS5k=","passwd":"mysupersecretpasswordtobestored!!!","kdf-algo":"Argon2"}'| json_pp
```


|Name|Type|Description|
|---|---|---|
|`base64-salt`<BR>(Required)|String|Salt to use to compute password hash for storage|
|`kdf-algo`<BR>(Optional)|String|KDF Algorithm to be used to compute the password hash for storage. **Valid Values:**: pbkdf, bcrypt, scrypt, argon2. Default Argon2.|
|`passwd`<BR>(Required)|String|User password to be storage. NEVER store it in plain text, store the computed hash|


Response:

```plaintext
{
   "base64-kdf-passwd-hash" : "t0YePwK9TPbtnGXqvwYU8Msp7iKLzuqdQ4VcGyW9T+s=",
   "base64-salt" : "I3+v64AxbXK9qVrvOV04H/fAHy5q/InjZe0791RzS5k=",
   "kdf-algo" : "Argon2",
   "passwd" : "mysupersecretpasswordtobestored!!!"
}
```
|Name|Type|Description|
|---|---|---|
|`base64-salt`|String|Salt to use for computing password hash for storage|
|`kdf-algo`|String|KDF Algorithm to be used to compute the password hash for storage|
|`passwd`|String|User password to be storage. NEVER store it in plain text, store the computed hash|
|`base64-kdf-passwd-hash`|String|Password Hash computed using `kdf-algo` & `base64-salt` to be stored|


