package com.sallet.cold.luna.bitcoinj;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Locale;
import org.bitcoinj.core.AddressFormatException.InvalidCharacter;
import org.bitcoinj.core.AddressFormatException.InvalidChecksum;
import org.bitcoinj.core.AddressFormatException.InvalidDataLength;
import org.bitcoinj.core.AddressFormatException.InvalidPrefix;

public class Bech32 {
    private static final String CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";
    private static final byte[] CHARSET_REV = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 15, -1, 10, 17, 21, 20, 26, 30, 7, 5, -1, -1, -1, -1, -1, -1, -1, 29, -1, 24, 13, 25, 9, 8, 23, -1, 18, 22, 31, 27, 19, -1, 1, 0, 3, 16, 11, 28, 12, 14, 6, 4, 2, -1, -1, -1, -1, -1, -1, 29, -1, 24, 13, 25, 9, 8, 23, -1, 18, 22, 31, 27, 19, -1, 1, 0, 3, 16, 11, 28, 12, 14, 6, 4, 2, -1, -1, -1, -1, -1};

    public Bech32() {
    }

    private static int polymod(byte[] values) {
        int c = 1;
        byte[] var2 = values;
        int var3 = values.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte v_i = var2[var4];
            int c0 = c >>> 25 & 255;
            c = (c & 33554431) << 5 ^ v_i & 255;
            if ((c0 & 1) != 0) {
                c ^= 996825010;
            }

            if ((c0 & 2) != 0) {
                c ^= 642813549;
            }

            if ((c0 & 4) != 0) {
                c ^= 513874426;
            }

            if ((c0 & 8) != 0) {
                c ^= 1027748829;
            }

            if ((c0 & 16) != 0) {
                c ^= 705979059;
            }
        }

        return c;
    }

    private static byte[] expandHrp(String hrp) {
        int hrpLength = hrp.length();
        byte[] ret = new byte[hrpLength * 2 + 1];

        for(int i = 0; i < hrpLength; ++i) {
            int c = hrp.charAt(i) & 127;
            ret[i] = (byte)(c >>> 5 & 7);
            ret[i + hrpLength + 1] = (byte)(c & 31);
        }

        ret[hrpLength] = 0;
        return ret;
    }

    private static boolean verifyChecksum(String hrp, byte[] values) {
        byte[] hrpExpanded = expandHrp(hrp);
        byte[] combined = new byte[hrpExpanded.length + values.length];
        System.arraycopy(hrpExpanded, 0, combined, 0, hrpExpanded.length);
        System.arraycopy(values, 0, combined, hrpExpanded.length, values.length);
        return polymod(combined) == 1;
    }

    private static byte[] createChecksum(String hrp, byte[] values) {
        byte[] hrpExpanded = expandHrp(hrp);
        byte[] enc = new byte[hrpExpanded.length + values.length + 6];
        System.arraycopy(hrpExpanded, 0, enc, 0, hrpExpanded.length);
        System.arraycopy(values, 0, enc, hrpExpanded.length, values.length);
        int mod = polymod(enc) ^ 1;
        byte[] ret = new byte[6];

        for(int i = 0; i < 6; ++i) {
            ret[i] = (byte)(mod >>> 5 * (5 - i) & 31);
        }

        return ret;
    }

    public static String encode(Bech32Data bech32) {
        return encode(bech32.hrp, bech32.data);
    }

    public static String encode(String hrp, byte[] values) {
        if (hrp.length() < 1) {
            throw new IllegalArgumentException("Human-readable part is too short");
        } else if (hrp.length() > 83) {
            throw new IllegalArgumentException("Human-readable part is too long");
        } else {
            hrp = hrp.toLowerCase(Locale.ROOT);
            byte[] checksum = createChecksum(hrp, values);
            byte[] combined = new byte[values.length + checksum.length];
            System.arraycopy(values, 0, combined, 0, values.length);
            System.arraycopy(checksum, 0, combined, values.length, checksum.length);
            StringBuilder sb = new StringBuilder(hrp.length() + 1 + combined.length);
            sb.append(hrp);
            sb.append('1');
            byte[] var5 = combined;
            int var6 = combined.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                byte b = var5[var7];
                sb.append("qpzry9x8gf2tvdw0s3jn54khce6mua7l".charAt(b));
            }

            return sb.toString();
        }
    }

    public static Bech32Data decode(String str) {
        boolean lower = false;
        boolean upper = false;
        if (str.length() < 8) {
            throw new InvalidDataLength("Input too short: " + str.length());
        } else if (str.length() > 90) {
            throw new InvalidDataLength("Input too long: " + str.length());
        } else {
            int pos;
            for(pos = 0; pos < str.length(); ++pos) {
                char c = str.charAt(pos);
                if (c < '!' || c > '~') {
                    throw new InvalidCharacter(c, pos);
                }

                if (c >= 'a' && c <= 'z') {
                    if (upper) {
                        throw new InvalidCharacter(c, pos);
                    }

                    lower = true;
                }

                if (c >= 'A' && c <= 'Z') {
                    if (lower) {
                        throw new InvalidCharacter(c, pos);
                    }

                    upper = true;
                }
            }

            pos = str.lastIndexOf(49);
            if (pos < 1) {
                throw new InvalidPrefix("Missing human-readable part");
            } else {
                int dataPartLength = str.length() - 1 - pos;
                if (dataPartLength < 6) {
                    throw new InvalidDataLength("Data part too short: " + dataPartLength);
                } else {
                    byte[] values = new byte[dataPartLength];

                    for(int i = 0; i < dataPartLength; ++i) {
                        char c = str.charAt(i + pos + 1);
                        if (CHARSET_REV[c] == -1) {
                            throw new InvalidCharacter(c, i + pos + 1);
                        }

                        values[i] = CHARSET_REV[c];
                    }

                    String hrp = str.substring(0, pos).toLowerCase(Locale.ROOT);
                    if (!verifyChecksum(hrp, values)) {
                        throw new InvalidChecksum();
                    } else {
                        return new Bech32Data(hrp, Arrays.copyOfRange(values, 0, values.length - 6));
                    }
                }
            }
        }
    }

    public static byte[] convert(byte[] data, int inBits, int outBits, boolean pad) {
        int value = 0;
        int bits = 0;
        int maxV = (1 << outBits) - 1;
        ByteArrayOutputStream result = new ByteArrayOutputStream(maxV + 1);
        byte[] var8 = data;
        int var9 = data.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            byte datum = var8[var10];
            value = value << inBits | datum & 255;
            bits += inBits;

            while(bits >= outBits) {
                bits -= outBits;
                result.write(value >> bits & maxV);
            }
        }

        if (pad) {
            if (bits > 0) {
                result.write(value << outBits - bits & maxV);
            }
        } else {
            if (bits >= inBits) {
                throw new IllegalStateException("Excess padding");
            }

            if ((value << outBits - bits & maxV) > 0) {
                throw new IllegalStateException("Non-zero padding");
            }
        }

        return result.toByteArray();
    }

    public static byte[] toWords(byte[] bytes) {
        return convert(bytes, 8, 5, true);
    }

    public static class Bech32Data {
        public final String hrp;
        public final byte[] data;

        private Bech32Data(String hrp, byte[] data) {
            this.hrp = hrp;
            this.data = data;
        }
    }
}
