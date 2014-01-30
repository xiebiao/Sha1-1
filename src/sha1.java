public class sha1 {


    /*
     *	Encode ASCII formatted string using secure hash algorithm
     *	@params data	byte array of string to be encoded
     */
    public static byte[] encode(byte[] data) {

        //pad byte array, assume ASCII encoding
        int orig_len = data.length;	//length in bytes
        long orig_len_bits = orig_len * 8;	//length in bits

        byte[] with_one = new byte[orig_len+1];			
        System.arraycopy(data, 0, with_one, 0, orig_len);
        with_one[with_one.length - 1] = (byte) 0x80;	//append 1
        int new_length = with_one.length*8;		//get new length in bits

        //find length multiple of 512
        while (new_length % 512 != 448) {		
            new_length += 8;
        }

        //size of block with appended zeros
        byte[] with_zeros = new byte[new_length/8];
        System.arraycopy(with_one, 0 , with_zeros, 0, with_one.length);

        //add 64 bits for original length
        byte[] output = new byte[with_zeros.length + 8];
        for (int i = 0; i < 8; i++) {
            output[output.length -1 - i] = (byte) ((orig_len_bits >>> (8 * i)) & 0xFF);
        } 
        System.arraycopy(with_zeros, 0 , output, 0, with_zeros.length);

        int size = output.length;
        int num_chunks = size * 8 /512;

        int h0 = 0x67452301;
        int h1 = 0xEFCDAB89;
        int h2 = 0x98BADCFE;
        int h3 = 0x10325476;
        int h4 = 0xC3D2E1F0;

        //hash each successive 512 chunk
        for (int i = 0; i < num_chunks; i++) {
            int[] w = new int[80];
            //divide into 16 32 bit words
            for (int j = 0; j < 16; j++) {
                w[j] =  ((output[i*512/8 + 4*j] << 24) & 0xFF000000) | ((output[i*512/8 + 4*j+1] << 16) & 0x00FF0000);
                w[j] |= ((output[i*512/8 + 4*j+2] << 8) & 0xFF00) | (output[i*512/8 + 4*j+3] & 0xFF);
            }

            //extend 16 words into 80 words
            for (int j = 16; j < 80; j++) {
                w[j] = left_rotate(w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16], 1);
            }	

            //initialize initial values

            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;
            int f = 0;
            int k = 0;
            //main loop
            for (int j = 0; j < 80; j++)
            {
                if (0 <= j && j <= 19) {
                    f = (b & c) | ((~b) & d);
                    k = 0x5A827999;
                }
                else if(20 <= j && j <= 39) {
                    f = b ^ c ^ d;
                    k = 0x6ED9EBA1;
                }
                else if(40 <= j && j <= 59) {
                    f = (b & c) | (b & d) | (c & d);
                    k = 0x8F1BBCDC;
                }
                else if(60 <= j && j <= 79) {
                    f = b ^ c ^ d;
                    k = 0xCA62C1D6;
                }

                int temp = left_rotate(a, 5) + f + e + k + w[j];
                e = d;
                d = c;
                c = left_rotate(b, 30);
                b = a;
                a = temp;
            }

            //add chunk's hash to result
            h0 = h0 + a;
            h1 = h1 + b;
            h2 = h2 + c;
            h3 = h3 + d;
            h4 = h4 + e;	
        }

        byte[] hash = new byte[20];
        for (int j = 0; j < 4; j++) {
            hash[j] = (byte) ((h0 >>> 24-j*8) & 0xFF);

        }
        for (int j = 0; j < 4; j++) {
            hash[j+4] = (byte) ((h1 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+8] = (byte) ((h2 >>> 24-j*8) & 0xFF);
        }
        for (int j = 0; j < 4; j++) {
            hash[j+12] = (byte) ((h3 >>> 24-j*8) & 0xFF);

        }
        for (int j = 0; j < 4; j++) {
            hash[j+16] = (byte) ((h4 >>> 24-j*8) & 0xFF);
        }

        return hash;

    }
    private static int left_rotate(int n, int d) {
        return (n << d) | (n >>> (32 - d));
    }
}
