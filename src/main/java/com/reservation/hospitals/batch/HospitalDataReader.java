package com.reservation.hospitals.batch;

import com.reservation.hospitals.controller.HospitalDataClient;
import com.reservation.hospitals.domain.Hospital;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.batch.item.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HospitalDataReader implements ItemStreamReader<Hospital> {
    private final HospitalDataClient hospitalDataClient;
    private Iterator<Hospital> hospitalIterator;

    public HospitalDataReader(HospitalDataClient hospitalDataClient) {
        this.hospitalDataClient = hospitalDataClient;
    }


    @Override
    public Hospital read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(hospitalIterator != null && hospitalIterator.hasNext()){
            return hospitalIterator.next();
        }else{
            return null;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        int hospitalTotalCount = hopitalTotalGetCount();
        List<Hospital> hospitalList = hospitalGetData(hospitalTotalCount);
        this.hospitalIterator = hospitalList.iterator();
    }

    private List<Hospital> hospitalGetData(int hospitalTotalCount) {
        String result = hospitalDataClient.getAllData(hospitalTotalCount);
        JSONObject jsonObject = XML.toJSONObject(result);
        JSONArray jsonHospitalArr = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
        List<Hospital> hospitalGetDataList = new ArrayList<>();
        for(int i = 0; i < jsonHospitalArr.length(); i++){
            JSONObject hospitalData = jsonHospitalArr.getJSONObject(i);
            Hospital hospotal = Hospital.builder()
                .addr(hospitalData.getString("addr"))
                .clCd(hospitalData.getInt("clCd"))
                .clCdNm(hospitalData.getString("clCdNm"))
                .emdongNm(hospitalData.optString("emdongNm",""))
                .estbDd(hospitalData.optString("estbDd",""))
                .hospUrl(hospitalData.optString("hospUrl",""))
                .postNo(hospitalData.getInt("postNo"))
                .sgguCd(hospitalData.getInt("sgguCd"))
                .sgguCdNm(hospitalData.getString("sgguCdNm"))
                .sidoCd(hospitalData.getInt("sidoCd"))
                .sidoCdNm(hospitalData.getString("sidoCdNm"))
                .telno(hospitalData.optString("telno",""))
                .yadmNm(hospitalData.getString("yadmNm"))
                .ykiho(hospitalData.getString("ykiho"))
                .build();
            hospitalGetDataList.add(hospotal);
        }
        return hospitalGetDataList;
    }

    private int hopitalTotalGetCount() {
        String result = hospitalDataClient.getCount();
        JSONObject jsonObject = XML.toJSONObject(result);
        return jsonObject.getJSONObject("response").getJSONObject("body").getInt("totalCount");
    }

    @Override
    public void close() throws ItemStreamException {
        this.hospitalIterator = null;
    }
}
