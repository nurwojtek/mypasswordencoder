package pl.com.nur.mypasswordencoder;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Mypasswordencoder implements PasswordEncoder {
    private int MAXLENGHT = 160;
    private String  password;

    @Override
    public String encode(CharSequence charSequence) {
        return myencode(charSequence);
    }

    @Override
    public boolean matches(CharSequence charSequence, String encodedPassword) {
        password = encode(charSequence);
        return password.equals(encodedPassword);
    }

    private String myencode(CharSequence charSequence) {
        // powielam hasło
        char[] tempPassword = new char[MAXLENGHT];
        int[] tempPasswordInt = new int[MAXLENGHT/4];
        char[] tempShortPass = new char[MAXLENGHT/4];
        int j=0;
        for (int i = 0; i < MAXLENGHT; i++) {
            if (j==charSequence.length()) {
                j=0;
            }
            tempPassword[i] = (char) (charSequence.charAt(j) + i);
            j++;
        }

        for (int i = 0; i < (MAXLENGHT) ; i+=2) {
            // skracam do stałej długości + hashuje
             tempPasswordInt[(int)i/4]= tempPasswordInt[(int)i/4] + (int) ((tempPassword[i]/4 + tempPassword[i+1]/4 + tempPassword[MAXLENGHT-i-1]/4 + tempPassword[MAXLENGHT-i-2]/4)* Math.sin(i*1.3)* Math.sin(i/3));
        }

        for (int i = 0; i <MAXLENGHT/4 ; i++) {
             int passInt = tempPasswordInt[i];
             // wpisuje się w podstawowe znaki z kodow ASCII
             if(passInt<0)
                 passInt=-passInt;
             if(passInt>'~')
                 passInt='~'-i;
             if(passInt<' ')
                 passInt= passInt + '!';
             tempShortPass[i]=(char) passInt;
        }
        return new String(tempShortPass);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        System.out.println(encode("ala ma kota"));
        System.out.println(encode("ala ma koty"));
        System.out.println(encode("Ala ma kota"));
        System.out.println(encode("Ala ma koty"));
        System.out.println(encode("1111"));
        System.out.println(encode("aaa"));
        System.out.println(encode("b"));
        System.out.println(encode(" "));

    }
}
