package aes128;
import java.util.Scanner;
public class AES128 
{
 static String[][] sbox={{"63","7C","77","7B","F2","6B","6F","C5","30","01","67","2B","FE","D7","AB","76"},
                        {"CA","82","C9","7D","FA","59","47","F0","AD","D4","A2","AF","9C","A4","72","C0"},
                        {"B7","FD","93","26","36","3F","F7","CC","34","A5","E5","F1","71","D8","31","15"},
		        {"04","C7","23","C3","18","96","05","9A","07","12","80","E2","EB","27","B2","75"},
			{"09","83","2C","1A","1B","6E","5A","A0","52","3B","D6","B3","29","E3","2F","84"},
			{"53","D1","00","ED","20","FC","B1","5B","6A","CB","BE","39","4A","4C","58","CF"},
			{"D0","EF","AA","FB","43","4D","33","85","45","F9","02","7F","50","3C","9F","A8"},
                        {"51","A3","40","8F","92","9D","38","F5","BC","B6","DA","21","10","FF","F3","D2"}, 
                        {"CD","0C","13","EC","5F","97","44","17","C4","A7","7E","3D","64","5D","19","73"}, 
                        {"60","81","4F","DC","22","2A","90","88","46","EE","B8","14","DE","5E","0B","DB"}, 
                        {"E0","32","3A","0A","49","06","24","5C","C2","D3","AC","62","91","95","E4","79"}, 
                        {"E7","C8","37","6D","8D","D5","4E","A9","6C","56","F4","EA","65","7A","AE","08"},
                        {"BA","78","25","2E","1C","A6","B4","C6","E8","DD","74","1F","4B","BD","8B","8A"}, 
                        {"70","3E","B5","66","48","03","F6","0E","61","35","57","B9","86","C1","1D","9E"},
                        {"E1","F8","98","11","69","D9","8E","94","9B","1E","87","E9","CE","55","28","DF"}, 
                        {"8C","A1","89","0D","BF","E6","42","68","41","99","2D","0F","B0","54","BB","16"}};
 static String[][] keyout=new String[4][4];
 static String[] w0=new String[4],w1=new String[4],w2=new String[4],w3=new String[4],y1=new String[4],z1=new String[4],
                 w4=new String[4],w5=new String[4],w6=new String[4],w7=new String[4];
 static String[][] rcon={{"2A","90","88","46"},
                        {"FB","43","4D","33"},
                        {"68","41","99","2D"},
                        {"FE","D7","AB","76"},
                        {"2E","1C","A6","B4"},
                        {"D9","8E","94","9B"},
                        {"26","36","3F","F7"},
                        {"0D","BF","E6","42"},
                        {"BA","78","25","2E"},
                        {"B0","54","BB","16"}};
 
 static String[][] gf={{"02","03","01","01"},
                       {"01","02","03","01"},
                       {"01","01","02","03"},
                       {"03","01","01","02"}};
 
 public static void main(String[] args)
  {
        Scanner ip=new Scanner(System.in);
        
        System.out.println("Enter the 16-character message:");
        String text=ip.nextLine();
        int c=0,p=-1;
        String[][] msg=new String[4][4];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                p++;
                c=text.charAt(p);
                if(c%16>9)
                msg[i][j]=(c/16)+""+(char)(c%16+55);
                else
                msg[i][j]=(c/16)+""+(c%16);
            }
         }
        
        System.out.println("Enter the 16-character key:");
        text=ip.nextLine();
        c=0;p=-1;
        String[][] key=new String[4][4];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                p++;
                c=text.charAt(p);
                if(c%16>9)
                key[i][j]=(c/16)+""+(char)(c%16+55);
                else
                key[i][j]=(c/16)+""+(c%16);
            }
         }
        
        String[][]  tran=key;
        String[][] keyin=new String[4][4];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            keyin[j][i]=tran[i][j];
           
        }
        
        System.out.println("\nThe Plain Text Matrix is:");
        for(int i=0;i<4;i++)
        {
          for(int j=0;j<4;j++)
            System.out.print(msg[i][j]+" ");
        System.out.println();
        }
        System.out.println("\nThe Key In Matrix is:");
        for(int i=0;i<4;i++)
        {
          for(int j=0;j<4;j++)
            System.out.print(keyin[i][j]+" ");
        System.out.println();
        }
        
        String[][] state=new String[4][4];
        for(int i=0;i<4;i++)
        {
            state[i]=xorer(msg[i],keyin[i]);
            state[i]=sboxLookUp(state[i]);
        }
        state=shiftRows(state);
        state=mixColumns(state);
        for(int i=0;i<4;i++)
        state[i]=xorer(state[i],(keyRoundGenerator(keyin))[i]);
        
        keyin=keyRoundGenerator(keyin);
      
        for(int i=1;i<=8;i++)
        {
            for(int j=0;j<4;j++)
                state[j]=sboxLookUp(state[j]);
           
            state=shiftRows(state);
            
            state=mixColumns(state);
            
            for(int j=0;j<4;j++)
                state[j]=xorer(state[j],(keyRoundGenerator(keyin))[j]);
            
            keyin=keyRoundGenerator(keyin);
            
        }
        for(int j=0;j<4;j++)
                state[j]=sboxLookUp(state[j]);
        
        state=shiftRows(state);
        
        for(int j=0;j<4;j++)
                state[j]=xorer(state[j],(keyRoundGenerator(keyin))[j]);
        
        System.out.println("\nThe Cipher Matrix is:");
        for(int i=0;i<4;i++)
        {
          for(int j=0;j<4;j++)
            System.out.print(state[i][j]+" ");
        System.out.println();
        }
        
        System.out.println("\nThe Cipher is:");
        for(int i=0;i<4;i++)
        {
          int a,b,h;
          for(int j=0;j<4;j++)
          {
            a=state[i][j].charAt(0);
            a=(a<58)?(a-48):(a-55);
            b=state[i][j].charAt(1);
            b=(b<58)?(b-48):(b-55);
            h=(16*b)+a;
              System.out.print((char)h);
              
          }
        }
        System.out.println();
  }
 public static String[][] mixColumns(String[][] matrix)
        {
        for(int i=0;i<4;i++)
        {
           w0[i]=matrix[i][0];
           w1[i]=matrix[i][1];
           w2[i]=matrix[i][2];
           w3[i]=matrix[i][3];
        }
        
        w4=ander(gf[0],w0);
        w5=ander(gf[1],w1);
        w6=ander(gf[2],w2);
        w7=ander(gf[3],w3);
        
        String[][] w8={{w4[0],"00","00","00"},
                      {w4[1],"00","00","00"},
                      {w4[2],"00","00","00"},
                      {w4[3],"00","00","00"}};
        
        String[] w9=xorer(xorer(xorer(w8[0],w8[1]),w8[2]),w8[3]);
        
        String[][] w10={{w5[0],"00","00","00"},
                      {w5[1],"00","00","00"},
                      {w5[2],"00","00","00"},
                      {w5[3],"00","00","00"}};
        
        String[] w11=xorer(xorer(xorer(w10[0],w10[1]),w10[2]),w10[3]);
        
        String[][] w12={{w6[0],"00","00","00"},
                      {w6[1],"00","00","00"},
                      {w6[2],"00","00","00"},
                      {w6[3],"00","00","00"}};
        
        String[] w13=xorer(xorer(xorer(w12[0],w12[1]),w12[2]),w12[3]);
        
        String[][] w14={{w7[0],"00","00","00"},
                      {w7[1],"00","00","00"},
                      {w7[2],"00","00","00"},
                      {w7[3],"00","00","00"}};
        
        String[] w15=xorer(xorer(xorer(w14[0],w14[1]),w14[2]),w14[3]);
        
        String[][] w16={{w9[0],w11[0],w13[0],w15[0]},
                        {w9[1],w11[1],w13[1],w15[1]},
                        {w9[2],w11[2],w13[2],w15[2]},
                        {w9[3],w11[3],w13[3],w15[3]}};
        
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            matrix[j][i]=w16[i][j];
           
        }
        return matrix;
        
        }  
 public static String[][] shiftRows(String[][] matrix)
 {
    String tempElement=matrix[1][0],tempElement1=matrix[2][0],tempElement2=matrix[2][1],tempElement3=matrix[3][3];
    String[] tempArray1={matrix[1][1],matrix[1][2],matrix[1][3],tempElement};
    matrix[1]=tempArray1; 
    String[] tempArray2={matrix[2][2],matrix[2][3],tempElement1,tempElement2};
    matrix[2]=tempArray2;
    String[] tempArray3={tempElement3,matrix[3][0],matrix[3][1],matrix[3][2]};
    matrix[3]=tempArray3;
    
    return matrix;
 }
 public static String[][] keyRoundGenerator(String[][] matrix)
  {
      for(int i=0;i<4;i++)
        {
           w0[i]=matrix[i][0];
           w1[i]=matrix[i][1];
           w2[i]=matrix[i][2];
           w3[i]=matrix[i][3];
        }
      String tempElement=w3[0];
      for(int i=0;i<3;i++)
       w3[i]=w3[i+1];
      w3[3]=tempElement;
        
      y1=sboxLookUp(w3);
      
      z1=xorer(y1,rcon[0]);
      w4=xorer(z1,w0);
      w5=xorer(w4,w1);
      w6=xorer(w5,w2);
      w7=xorer(w6,w3);
      
      String[][] tempKeyOut={w4,w5,w6,w7};
      for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            keyout[j][i]=tempKeyOut[i][j];
        }
      return keyout;
  }
 public static String[] sboxLookUp(String[] x)
      {
      int a,b;
      String[] z=new String[4];
      for(int i=0;i<4;i++) 
      {
         a=x[i].charAt(0);
         a=(a<58)?(a-48):(a-55);
         b=x[i].charAt(1);
         b=(b<58)?(b-48):(b-55);
         z[i]=sbox[a][b];
      }
      return z;
      }
 public static String[] xorer(String[] x,String[] y){
        String[] z=new String[4];
        int p,q,r,s,t,u;
        for(int i=0;i<4;i++)
        {
         p=x[i].charAt(0);
         p=(p<58)?(p-48):(p-55);
         q=x[i].charAt(1);
         q=(q<58)?(q-48):(q-55);
         r=y[i].charAt(0);
         r=(r<58)?(r-48):(r-55);
         s=y[i].charAt(1);
         s=(s<58)?(s-48):(s-55);
         
         t=p^r;
         u=q^s;
         
         if(t>9&&u>9)
             z[i]=(char)(t+55)+""+(char)(u+55);
         else if(t>9&&u<10)
             z[i]=(char)(t+55)+""+u;
         else if(t<10&&u>9)
             z[i]=t+""+(char)(u+55);
         else
             z[i]=t+""+u;
        }
        return z;
    }
 public static String[] ander(String[] x,String[] y){
        String[] z=new String[4];
        int p,q,r,s,t,u;
        for(int i=0;i<4;i++)
        {
         p=x[i].charAt(0);
         p=(p<58)?(p-48):(p-55);
         q=x[i].charAt(1);
         q=(q<58)?(q-48):(q-55);
         r=y[i].charAt(0);
         r=(r<58)?(r-48):(r-55);
         s=y[i].charAt(1);
         s=(s<58)?(s-48):(s-55);
         
         t=p&r;
         u=q&s;
         
         if(t>9&&u>9)
             z[i]=(char)(t+55)+""+(char)(u+55);
         else if(t>9&&u<10)
             z[i]=(char)(t+55)+""+u;
         else if(t<10&&u>9)
             z[i]=t+""+(char)(u+55);
         else
             z[i]=t+""+u;
        }
        return z;
     }
 }
           
    
    

