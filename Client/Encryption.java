package chatmain;

public class Encryption {
    private char[] alfaU, alfaL, alfaS, alfaI;
    private int rand;
    private char cap;
    private int pos;
    
    public Encryption() {
        alfaU = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        alfaL = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        alfaI = new char[]{'~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '+' , '=', '[', '{', ']', '}', ':', ';', '<', '>', ',', '.', '?'};
        alfaS = new char[26];
        cap = '`';
        rand = (int)(Math.random() * (3) + 1);
        rand *= 2;
        pos = 1;
        for(int i = 0; i < 26; i++) {
            if(pos >= 26) {
                pos -= 26;
            }
            alfaS[i] = alfaU[pos];
            pos += rand + 1;
            System.out.print(alfaS[i] + " ");
        }
        System.out.println("");
    }
    
    public String encrypt(String a) {
        boolean modified = false;
        String temp = "";
        temp += rand;
        for(int i = 0; i < a.length(); i++) {
            for(int j = 0; j < 26; j++) {
                if(alfaU[j] == a.charAt(i)) {
                    temp += cap;
                    for(int k = 0; k < 26; k++) {
                        if(alfaS[k] == alfaU[j]) {
                            temp += alfaI[k];
                            modified = true;
                        }
                    }
                    break;
                }
                else if(alfaL[j] == a.charAt(i)) {
                    for(int k = 0; k < 26; k++) {
                        if(Character.toLowerCase(alfaS[k]) == alfaL[j]) {
                            temp += alfaI[k];
                            modified = true;
                        }
                    }
                    break;
                }
                else if(alfaI[j] == a.charAt(i)) {
                    temp += alfaS[j];
                    modified = true;
                    break;
                }
                else if(cap == a.charAt(i)) {
                    temp += cap + cap;
                    modified = true;
                    break;
                }
            }
            if(modified == false) {
                temp += a.charAt(i);
                modified = false;
            }
            else {
                modified = false;
            }
        }
        return temp;
    }
    
    public String decrypt(String a) {
        String temp = "";
        boolean modified = false;
        int randTemp = Character.getNumericValue(a.charAt(0));
        pos = 1;
        for(int i = 0; i < 26; i++) {
            if(pos >= 26) {
                pos -= 26;
            }
            alfaS[i] = alfaU[pos];
            pos += randTemp + 1;
        }
        for(int i = 1; i < a.length(); i++) {
            for(int j = 0; j < 26; j++) {
                if(a.charAt(i) == alfaI[j]) {
                    for(int k = 0; k < 26; k++) {
                        if(Character.toUpperCase(alfaL[k]) == alfaS[j]) {
                            temp += alfaL[k];
                            modified = true;
                        }
                    }
                }
                else if(a.charAt(i) == alfaS[j]) {
                    temp += alfaI[j];
                    modified = true;
                }
                else if(a.charAt(i) == cap) {
                    if(a.charAt(i+1) == alfaI[j]) {
                        for(int k = 0; k < 26; k++) {
                            if(alfaU[k] == alfaS[j]) {
                                temp+= alfaU[k];
                                i++;
                                modified = true;
                            }
                        } 
                    }
                    else if(a.charAt(i+1) == cap) {
                        temp += cap;
                        i++;
                        modified = true;
                    }
                }
            }
            if(modified == false) {
                temp += a.charAt(i);
                modified = false;
            }
            else {
                modified = false;
            }
        }
        pos = 1;
        for(int i = 0; i < 26; i++) {
            if(pos >= 26) {
                pos -= 26;
            }
            alfaS[i] = alfaU[pos];
            pos += rand + 1;
        }
        return temp;
    }
    
}
