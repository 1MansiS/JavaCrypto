# Generate Digital Signatures

## Generating Asymmetric Key

Endpoint:

```plaintext
GET /generate-digital-signature-parameters/{digital_signature_algo}
```

Sample Request:

```plaintext
curl -X POST 'http://localhost:8080/generate-digital-signature-parameters' | json_pp
```

OR 

```
curl -X POST 'http://localhost:8080/generate-digital-signature-parameters/eddsa' | json_pp
```

|Name|Type|Description|
|---|---|---|
|`digital_signature_algo`<BR>(Optional)|String|Signature algorithm to use. Accepted values are `ecdsa` or `ec-curve`. Defaults to ed-curve|


Response:

```plaintext
{
   "digital_signature_algo" : "ed-curve",
   "base64_private_key" : "MC4CAQAwBQYDK2VwBCIEIOcF/tgt18Cv1F3i3Jw7SJL3RgbjeO9NxrfSOxVBIJ1B",
   "base64_public_key" : "MCowBQYDK2VwAyEADzMUhYWEdBCl6XUlc5/PT7BuvFjFayaaCp2ktatJRxc="
}
```

|Name|Type|Description|
|---|---|---|
|`digital_signature_algo`|String|Digital Signature Algorithm Used|
|`base64_private_key`|String|Generated Private Key Base 64 encoded|
|`base64_public_key`|String|Generated Public Key Base 64 encoded|

## Generating the Signature:

Endpoint:

```plaintext
POST /sign
```

Sample Request:

```plaintext
curl -X POST 'http://localhost:8080/sign' -H  "Content-Type: application/json" \
-d '{"digital_signature_algo":"ed-curve","base64_private_key":"MC4CAQAwBQYDK2VwBCIEIEkeQ5bKh7NQt5TxRhIEaSkpB7XY4KFZ8M9zsG0heVPS","plaintext_message":"Hello World!"}' | json_pp
```


|Name|Type|Description|
|---|---|---|
|`digital_signature_algo`<BR>(Optional)|String|Signature Algorithm to be used. Accepted values `ecdsa` or `ed-curve`. Defaults to `ed-curve`|
|`base64_private_key`<BR>(Required)|String|Generated Private key from above step to be used to sign the message|
|`plaintext_message`<BR>(Required)|String|Message to be signed|


Response:

```plaintext
{
   "base64_private_key" : "MC4CAQAwBQYDK2VwBCIEIAWrd/47VIbIFtbOE34Kwsj8Is1FsLBSXDpUMNpAZ/H1",
   "base64_sign" : "9dIBdU4Gjjb0BDtfmWIF1jl3ODbrAdaCjUlTfO3/JGxQu4VUW6LBiZrTTaeLMZhRdnQ+uw07uogIZey1iGlaAw==",
   "digital_signature_algo" : "ed-curve",
   "plaintext_message" : "Hello World!"
}
```

|Name|Type|Description|
|---|---|---|
|`digital_signature_algo`|String|Signature Algorithm to be used. Accepted values `ecdsa` or `ed-curve`. Defaults to `ed-curve`|
|`base64_private_key`|String|Generated Private key from above step to be used to sign the message|
|`plaintext_message`|String|Message to be signed|
|`base64_sign`|String|Signature Generated|

## Verifying the Signature:

```plaintext
POST /verify
```

Sample Request:

```plaintext
curl -X POST 'http://localhost:8080/verify' -H  "Content-Type: application/json" \
-d '{"plaintext_message":"Hello World!","base64_public_key":"MCowBQYDK2VwAyEAJF7iwf5nmWq+5znvKj1+F4ILsKRK6QUYmEIocUNFLFc=","base64_sign":"9dIBdU4Gjjb0BDtfmWIF1jl3ODbrAdaCjUlTfO3/JGxQu4VUW6LBiZrTTaeLMZhRdnQ+uw07uogIZey1iGlaAw=="}' | json_pp
```


|Name|Type|Description|
|---|---|---|
|`digital_signature_algo`<BR>(Optional)|String|Signature Algorithm to be used. Accepted values `ecdsa` or `ed-curve`. Defaults to `ed-curve`|
|`plaintext_message`<BR>(Required)|String|Received message to be verified|
|`base64_public_key`<BR>(Required)|String|Corresponding Public Key to verify the message|
|`base64_sign`<BR>(Required)|String|Signature of signed message received.|

Response:

```plaintext
{
   "base64_public_key" : "MCowBQYDK2VwAyEAq6ATMBErxIChQkN65+kWXr5kTXltNSkU2K7kkK/OEdU=",
   "base64_sign" : "N6JMKDPJZTfmlwVga2/vJrLrnIWHmcAmIS7hUUPo8qeST3R0b5LUosYBzknbRcdj3km0zC0P195VHkd0dSJQAA==",
   "digital_signature_algo" : "ed-curve",
   "plaintext_message" : "Hello World!",
   "verified" : true
}
```

|Name|Type|Description|
|---|---|---|
|`digital_signature_algo`|String|Signature Algorithm to be used. Accepted values `ecdsa` or `ed-curve`. Defaults to `ed-curve`|
|`plaintext_message`|String|Received message to be verified|
|`base64_public_key`|String|Corresponding Public Key to verify the message|
|`base64_sign`|String|Signature of signed message received.|
|`verified`|boolean|Is the signature valid, to esablish integrity, authenticity and non-repudiation|



