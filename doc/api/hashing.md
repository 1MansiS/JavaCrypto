# Generating a Hash:

Endpoint:

```plaintext
POST /digest
```


Sample Request:

```plaintext
curl 'http://localhost:8080/digest' \
-X POST -H "Content-type: application/json" \
-d '{"message":"Hello Hashing!!!","hashing-algo":"SHA3256"}' | json_pp
```

|Name|Type|Description|
|---|---|---|
|`message`<BR>(Required)|String|Message to be hashed|
|`hashing-algo`<BR>(Optional)|String|Hashing algorithm to be used. Possible values from [Java Security Standard Algorithm Names](https://docs.oracle.com/en/java/javase/18/docs/specs/security/standard-names.html#messagedigest-algorithms). Default Value: SHA-512|


Response:

```plaintext
{
   "hash" : "Mb9x21/z6XCh3OiwzWSfkxnybuKPRe0FiSqxLDkNDGRPcRzcvEHUrSRF6iseByz/qVtgXc3qYe4U1gWZkM2B7A==",
   "hashing-algo" : "SHA3-512",
   "message" : "Hello Hashing!!!"
}
```

|Name|Type|Description|
|---|---|---|
|`message`|String|Message to be hashed|
|`hashing-algo`|String|Hashing algorithm to be used. Possible values from [Java Security Standard Algorithm Names](https://docs.oracle.com/en/java/javase/18/docs/specs/security/standard-names.html#messagedigest-algorithms). Default Valye: SHA-512|
|`hash`|String|Computed Hash value of above `message`|
