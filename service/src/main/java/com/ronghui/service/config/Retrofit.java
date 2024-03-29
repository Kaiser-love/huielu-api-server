package com.ronghui.service.config;


import com.ronghui.server.network.RetrofitFactory;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public class Retrofit {

    private static APIFunction mAPIFunction = RetrofitFactory.build(APIFunction.class);

    public static APIFunction API() {
        return mAPIFunction;
    }

    public interface APIFunction {
        @Streaming
        @GET
        Observable<ResponseBody> donloadPic(@Url String picUrl);
    }

//    //网络请求 demo
//    public static void main(String[] args) {
//        ArrayList<String> imgList = new ArrayList<>();
//        imgList.add("https://starfire.site/pic/1d32fa3e2ded5bc557aca6cc3d9a6d72?p=0");
//        imgList.add("https://starfire.site/pic/78db0e060fb87cc2f326dbf6194315d5?p=0");
//        imgList.add("https://starfire.site/pic/b5b4c20a10bf3643d8a7bac682d19d62?p=0");
//        imgList.add("https://starfire.site/pic/ace218a59f4983f746ce1d05752922c0?p=0");
//
//        try {
//            String fileName = "tdsdt.pptx";
//            System.out.println(Retrofit.API().testPdf(fileName, imgList).execute().body().toString());
//            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
//            Message<String> messageS = Retrofit.API().testGet(fileName).execute().body();
//            if (messageS.getCode() == 0) {
//                byte[] message = Base64.getDecoder().decode(messageS.getBody());
//                fileOutputStream.write(message);
//            } else {
//                System.out.println("啥都没有" + messageS.getMsg());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}