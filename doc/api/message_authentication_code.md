# Computing Hashed Message Authentication Code (MAC)

## Compute HMAC Symmetric Key


Endpoint:

```plaintext
GET /compute-hmac-key/{hmac_enc_algo}
```

Sample Request:

```plaintext
curl 'http://localhost:8080/compute-hmac-key/' -X POST -H "Content-type: application/json" | json_pp
```

or 

```plaintext
curl 'http://localhost:8080/compute-hmac-key/HmacSHA256' -X POST -H "Content-type: application/json" -s | json_pp
```

|Name|Type|Description|
|---|---|---|
|`hmac_enc_algo`<BR>(Optional)|String|Hmac algorithm to be used. Possible values to be choosed from [Java Security Standard Algorithm Names](https://docs.oracle.com/en/java/javase/18/docs/specs/security/standard-names.html#mac-algorithms)|


Response:

```plaintext
{
   "base64-symmetric-key" : "l+fgI0xKB/hjZTBfrXf09HxWhLdsDyzpGzA7jZf/5jo="
}
```

|Name|Type|Description|
|---|---|---|
|`base64-symmetric-key`|String|Symmetric Key for computing MAC|

## Compute HMAC

```plaintext
POST /compute-hmac
```

Sample Request:

```plaintext
curl 'http://localhost:8080/compute-hmac' -X POST -H "Content-type: application/json" \
-d '{"message":"Hello MAC!!!","base64-symmetric-key":"l+fgI0xKB/hjZTBfrXf09HxWhLdsDyzpGzA7jZf/5jo="}' | json_pp
```


|Name|Type|Description|
|---|---|---|
|`message`<BR>(Required)|String|Input message whose MAC needs to be computed|
|`base64-symmetric-key`<BR>(Required)|String|Symmetric Key for computing MAC|


Response:

```plaintext
{
   "base64-symmetric-key" : "l+fgI0xKB/hjZTBfrXf09HxWhLdsDyzpGzA7jZf/5jo=",
   "mac" : "9BC5MixuWp6l1yiPB0ZmlsYp5bmnk2W4+U+GeFNQglv+pMhEJ2/tmudBumRvCfwhGCJ3L93xgD1oIzqe+tlN9w==",
   "message" : "Hello MAC!!!"
}
```

|Name|Type|Description|
|---|---|---|
|`message`|String|Input message whose MAC needs to be computed|
|`base64-symmetric-key`|String|Symmetric Key for computing MAC|
|`mac`|String|Computed MAC|