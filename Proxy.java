import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.StringTokenizer;

public class Proxy {
    int port;
    ServerSocket server;

    Proxy(int port){
        this.port=port;
        try {
            server= new ServerSocket(this.port);
            System.out.println("Listening for connection on port: "+this.port+" ...");
        } catch (IOException e) {
            System.out.println("There was an error: "+ e);
        }
    }

    void start(){
        while(true){
            try {
                String response;
                final Socket client = server.accept();
                InputStreamReader isr =  new InputStreamReader(client.getInputStream());
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(client.getOutputStream()), "UTF-8"));
                BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();
                while (line!=null  && !line.isEmpty()) {
                    System.out.println(line);
                    if(line.contains("GET")){
                        //System.out.println("<<<<<<<<<<<<<<<<<RESPONSE>>>>>>>>>>>>>>>>>>");
                        //System.out.println("URL------>>>>>"+this.tokenRequest(line));
                        response=this.sendGET(this.tokenRequest(line));
                        System.out.println("<<<<<<<<<<<<Response: ");
                        System.out.println(response);
                        out.write(response);
                        out.flush();
                        out.close();
                        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
     String sendGET(String url) throws IOException {
        URL obj = new URL(url);
        StringBuffer response = new StringBuffer();
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestProperty("User-Agent", "");
        int responseCode = con.getResponseCode();
         System.out.println(con.getHeaderField(0));
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            //System.out.println(response.toString());
        }
        return response.toString();
    }

    String tokenRequest(String request){
        String[]tokens=request.split("\\s");
        for (int x=0; x<tokens.length; x++)
            System.out.println(tokens[x]);
        return tokens[1];
    }
}
