#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>



/*
 *  Sha1 Hash algorithm 
 *  Eric Sun 2/3/2014
 */

class Sha1{
    public:

        unsigned int* encode(char* data) {
            unsigned int origLen = strlen(data);
            uint64_t origLenBits = origLen * 8;

            char* withOne = (char *)malloc((origLen+1));

            for (int i = 0; i < origLen; i++) {
                withOne[i] = data[i];
            }

            withOne[origLen] = 0x80;

            unsigned int newBitLen = (origLen+1) * 8;

            while (newBitLen % 512 != 448)
                newBitLen += 8;

            char* output = (char *)malloc((newBitLen/8 + 8));
            for (int i = 0; i < (newBitLen/8 + 8); i++) {
                output[i] = 0;
            }

            for (int i = 0; i < (origLen + 1); i++) {
                output[i] = *(withOne+i);
            }

            unsigned int outputLen = newBitLen/8 + 8;
            for (int i = 0; i < 8; i++) {
                output[outputLen -1 - i] = ((origLenBits >> (8 * i)) & 0xFF);
            }

            unsigned int num_chunks = outputLen * 8 / 512;

            unsigned int h0 = 0x67452301;
            unsigned int h1 = 0xEFCDAB89;
            unsigned int h2 = 0x98BADCFE; 
            unsigned int h3 = 0x10325476;
            unsigned int h4 = 0xC3D2E1F0;

            for(int i = 0; i < num_chunks; i++) {
                unsigned int* w = (unsigned int *) malloc(sizeof(int) * 80);
                for (int j = 0; j < 80; j++) {
                    w[j] = 0;
                }

                for (int j = 0; j < 16; j++) {
                    w[j] =  ((output[i*512/8 + 4*j] << 24) & 0xFF000000) | ((output[i*512/8 + 4*j+1] << 16) & 0x00FF0000);
                    w[j] |= ((output[i*512/8 + 4*j+2] << 8) & 0xFF00) | (output[i*512/8 + 4*j+3] & 0xFF);
                }

                for (int j = 16; j < 80; j++) {
                    w[j] = leftRotate(w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16], 1);
                }

                unsigned int a = h0;
                unsigned int b = h1;
                unsigned int c = h2;
                unsigned int d = h3;
                unsigned int e = h4;
                unsigned int f = 0;
                unsigned int k = 0;

                for(int j = 0; j < 80; j++) {
                    if (0 <= j && j <= 19) {
                        f = (b & c) | ((~b) & d);    
                        k = 0x5A827999;
                    }
                    else if (20 <= j && j <= 39) {
                        f = b ^ c ^ d; 
                        k = 0x6ED9EBA1;
                    }
                    else if (40 <= j && j <= 59) {
                        f = (b & c) | (b & d) | (c & d);
                        k = 0x8F1BBCDC;
                    }
                    else if(60 <= j && j <= 79) {
                        f = b ^ c ^ d;
                        k = 0xCA62C1D6;
                    }

                    unsigned int temp = leftRotate(a, 5) + f + e + k + w[j];
                    e = d;
                    d = c;
                    c = leftRotate(b, 30);
                    b = a;
                    a = temp;
                }

                h0 = h0 + a;
                h1 = h1 + b;
                h2 = h2 + c;
                h3 = h3 + d;
                h4 = h4 + e;
            }

            unsigned int* hash = (unsigned int *)malloc(sizeof(int) * 5);
            hash[0] = h0;
            hash[1] = h1;
            hash[2] = h2;
            hash[3] = h3;
            hash[4] = h4;
            return hash;
        }

        unsigned int leftRotate(unsigned int n,  unsigned int d) {
            return (n << d) | (n >> (32-d));
        }

};

uint64_t rightRotate(uint64_t n, uint64_t d) {
        return (n >> d) | (n << (64-d));
    }

int main() {
    char* data = "abc";

    Sha1 sha;
    unsigned int * hash = sha.encode(data);
    printf("%x%x%x%x%x\n", hash[0], hash[1], hash[2], hash[3], hash[4]);

}
