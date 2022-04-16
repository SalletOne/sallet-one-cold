package com.sallet.cold.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KDFBean {
    @JsonProperty("address")
    private String address;
    @JsonProperty("id")
    private String id;
    @JsonProperty("version")
    private int version;
    @JsonProperty("crypto")
    private CryptoDTO crypto;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public CryptoDTO getCrypto() {
        return crypto;
    }

    public void setCrypto(CryptoDTO crypto) {
        this.crypto = crypto;
    }

    public static class CryptoDTO {
        @JsonProperty("cipher")
        private String cipher;
        @JsonProperty("ciphertext")
        private String ciphertext;
        @JsonProperty("cipherparams")
        private CipherparamsDTO cipherparams;
        @JsonProperty("kdf")
        private String kdf;
        @JsonProperty("kdfparams")
        private KdfparamsDTO kdfparams;
        @JsonProperty("mac")
        private String mac;

        public String getCipher() {
            return cipher;
        }

        public void setCipher(String cipher) {
            this.cipher = cipher;
        }

        public String getCiphertext() {
            return ciphertext;
        }

        public void setCiphertext(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        public CipherparamsDTO getCipherparams() {
            return cipherparams;
        }

        public void setCipherparams(CipherparamsDTO cipherparams) {
            this.cipherparams = cipherparams;
        }

        public String getKdf() {
            return kdf;
        }

        public void setKdf(String kdf) {
            this.kdf = kdf;
        }

        public KdfparamsDTO getKdfparams() {
            return kdfparams;
        }

        public void setKdfparams(KdfparamsDTO kdfparams) {
            this.kdfparams = kdfparams;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public static class CipherparamsDTO {
            @JsonProperty("iv")
            private String iv;

            public String getIv() {
                return iv;
            }

            public void setIv(String iv) {
                this.iv = iv;
            }
        }

        public static class KdfparamsDTO {
            @JsonProperty("dklen")
            private int dklen;
            @JsonProperty("n")
            private int n;
            @JsonProperty("p")
            private int p;
            @JsonProperty("r")
            private int r;
            @JsonProperty("salt")
            private String salt;

            public int getDklen() {
                return dklen;
            }

            public void setDklen(int dklen) {
                this.dklen = dklen;
            }

            public int getN() {
                return n;
            }

            public void setN(int n) {
                this.n = n;
            }

            public int getP() {
                return p;
            }

            public void setP(int p) {
                this.p = p;
            }

            public int getR() {
                return r;
            }

            public void setR(int r) {
                this.r = r;
            }

            public String getSalt() {
                return salt;
            }

            public void setSalt(String salt) {
                this.salt = salt;
            }
        }
    }
}
