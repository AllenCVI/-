package com.kuxuan.moneynote.ui.activitys.exportbill;

import com.kuxuan.moneynote.api.ExceptionHandle;
import com.kuxuan.moneynote.api.MyObsever;
import com.kuxuan.moneynote.api.RetrofitClient;
import com.kuxuan.moneynote.json.ExportBillBean;
import com.kuxuan.moneynote.json.netbody.ExportBill;
import com.kuxuan.moneynote.listener.MVPListener;
import com.kuxuan.moneynote.utils.TimeUtlis;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Allence on 2018/3/15 0015.
 */

public class Model_ExportBill implements Contract_ExpoetBill.ExportBillModel{


    @Override
    public void exportBill(String email,String startTime,String endTime,String time,final MVPListener<ExportBill> listMVPListener) {

//        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("email",email);
//        hashMap.put("start_time", TimeUtlis.getTime(startTime, TimeUtlis.TIME_FORMAT_ONE));
//        hashMap.put("end_time",TimeUtlis.getTime(endTime, TimeUtlis.TIME_FORMAT_ONE));
//        hashMap.put("time",time);
//
//        Collection<String> keyset= hashMap.keySet();
//        List list=new ArrayList<String>(keyset);
//        Collections.sort(list);
//        String sign="";
//        for(int i=0;i<list.size();i++){
//
//            if(i!=list.size()-1) {
//                sign = sign + list.get(i)+"="+ hashMap.get(list.get(i)) + "&";
//            }else {
//                sign = sign + list.get(i)+"="+ hashMap.get(list.get(i));
//            }
//
//        }
//
//        sign = sign + "b693013c19222873eece0526b7b85da2";
//
//        sign = Md5Util.md5(sign);

        ExportBillBean exportBillBean = new ExportBillBean(email,TimeUtlis.getTime(startTime, TimeUtlis.TIME_FORMAT_ONE),TimeUtlis.getTime(endTime, TimeUtlis.TIME_FORMAT_ONE));

        RetrofitClient.getApiService().exportBill(exportBillBean).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObsever<ExportBill>() {
                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        listMVPListener.onFail(e.message);
                    }

                    @Override
                    public void onSuccess(ExportBill exportBill) {

                        if(exportBill.getCode()==0){
                             listMVPListener.onSuccess(exportBill);
                        }

                    }
                });


    }


}
