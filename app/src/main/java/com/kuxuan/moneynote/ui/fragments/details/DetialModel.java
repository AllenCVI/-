package com.kuxuan.moneynote.ui.fragments.details;

import android.text.TextUtils;
import android.util.Log;

import com.kuxuan.moneynote.MyApplication;
import com.kuxuan.moneynote.api.ApiHelper;
import com.kuxuan.moneynote.api.ApiService;
import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.common.Constant;
import com.kuxuan.moneynote.json.BaseJson;
import com.kuxuan.moneynote.json.UserAllBillJson;
import com.kuxuan.moneynote.json.netbody.SkinBean;
import com.kuxuan.moneynote.json.netbody.UserAllBillBody;
import com.kuxuan.moneynote.listener.MVPListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by xieshengqi on 2017/10/19.
 */

public class DetialModel implements DetialContract.DetialModel {
    @Override
    public void getDataLists(String year, String month, final MVPListener<UserAllBillJson> listMVPListener) {
        RetrofitClient.getApiService().getUserAllBill(new UserAllBillBody(year, month)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<BaseJson<UserAllBillJson>>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                listMVPListener.onFail(e.message);
            }

            @Override
            public void onSuccess(BaseJson<UserAllBillJson> userAllBillJsonBaseJson) {
                if (userAllBillJsonBaseJson.getCode() == Constant.Code_Request.SUCCESS_CODE) {

                    listMVPListener.onSuccess(userAllBillJsonBaseJson.getData());
                } else {
                    listMVPListener.onFail(userAllBillJsonBaseJson.getError().get(0));
                }

            }
        });
    }

    @Override
    public void getPopWindowData(final MVPListener<SkinBean> listMvpListener) {

        RetrofitClient.getApiService().getSkinData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObsever<SkinBean>() {
            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {

                Log.e("DetialModel", e.message);
            }

            @Override
            public void onSuccess(SkinBean skinBean) {

                if (skinBean != null) {
                    int code = skinBean.getCode();

                    if (code == 0) {
                        listMvpListener.onSuccess(skinBean);
                    } else {
                        listMvpListener.onFail(skinBean.getMessage().get(0));
                    }
                }


            }
        });


    }


    @Override
    public void getDataListsForDB(final String year, final String month, final HashMap<String, Integer> maps, final MVPListener<UserAllBillJson> listMVPListener) {
        io.reactivex.Observable.create(new ObservableOnSubscribe<UserAllBillJson>() {
            @Override
            public void subscribe(ObservableEmitter<UserAllBillJson> e) throws Exception {
                UserAllBillJson userBillJson = DetialDBOpertor.getInstance().getUserBillJson(Integer.parseInt(year), Integer.parseInt(month), maps);
                e.onNext(userBillJson);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UserAllBillJson>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UserAllBillJson userAllBillJson) {
                listMVPListener.onSuccess(userAllBillJson);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    String mPICPath;
    File mFile;

    @Override
    public void downLoadPic(String url, int size, int position, final DetialContract.DownloadListener downloadListener) {


        String fileheader = MyApplication.getInstance().getApplicationContext().getFilesDir().getPath() + "/" + "skin/";

        File file = new File(fileheader);
        if (!file.exists()) {
            file.mkdirs();
        }
        switch (size) {
            case 0:
                mPICPath = fileheader + Constant.Skin.SKINICON + position + ".png";
                break;
            case 1:
                mPICPath = fileheader + Constant.Skin.MINE + position + ".png";
                break;
            case 2:
                mPICPath = fileheader + Constant.Skin.HOMEPICNAME + position + ".png";
                break;
        }

        if (TextUtils.isEmpty(mPICPath)) {
            Log.e(TAG, "存储路径为空");
            return;
        }

        //建立一个文件
        mFile = new File(mPICPath);

        Call<ResponseBody> mCall = ApiHelper.getInstance().buildRetrofit().createService(ApiService.class).downloadPic(url);

        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {


                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Log.d("my",mPICPath);

                        writeFile2Disk(response, mFile, downloadListener);

                    }

                }).start();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                downloadListener.onFailure();

            }
        });

    }


    private void writeFile2Disk(Response<ResponseBody> response, File file, DetialContract.DownloadListener downloadListener) {


        Log.d("my1",mPICPath);

        downloadListener.onStart();
        long currentLength = 0;
        OutputStream os = null;

        InputStream is = null;

        try {
            is = response.body().byteStream(); //获取下载输入流
            long totalLength = response.body().contentLength();
            os = new FileOutputStream(file); //输出流
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
//                Log.e(TAG, "当前进度: " + currentLength);
                //计算当前下载百分比，并经由回调传出

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                downloadListener.onProgress((int) (100 * currentLength / totalLength));
                //当百分比为100时下载结束，调用结束回调，并传出下载后的本地路径
                if ((int) (100 * currentLength / totalLength) == 100) {
                    Log.d("my2",mPICPath);
                    downloadListener.onFinish(mPICPath); //下载完成
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

        } finally {
            if (os != null) {
                try {
                    os.close(); //关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close(); //关闭输入流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
