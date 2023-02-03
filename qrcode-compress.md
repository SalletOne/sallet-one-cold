### Sallet-one data transfer protocol and qrcode string compress

## data transfer protocol
sallet one use open protocol to transfer data between online APP and offline hardware APP. There are two types of protocol: address protocol and transaction protocol.

### address protocol
The address protocol format: odfp://{device-code}/{version}/address/{tokent-type}/{address}

for example Bitcoin address: odfp://{device-code}/1.0/address/1/1Mw91g5E8rWPCJUdy3MmT3bUKscu4tKFcp

### transaction protocol
The transaction protocol format: odfp://{device-code}/{version}/tx/{tokent-type}/{transaction}


## qrcode string compress
The raw transaction data (wait to sign transaction data and signed transaction data) is too big, and the generate qrcode is difficult to distinguish. so
sallet one use Deflater compression Algorithm. it is very useful to generate qrcode precisely. 

Here are online java online run env and Deflater compression and uncompress Algorithm.

[java online run env](https://www.programiz.com/java-programming/online-compiler/)

[java Deflater code](https://www.programiz.com/java-programming/online-compiler/)
