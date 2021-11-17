package com.grpc.poc.bff.transferstream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.grpc.poc.bff.LogUtil;
import com.grpc.poc.bff.dto.ResponseData;
import com.grpc.poc.proto.dto.qr.QrResponse;

import io.grpc.stub.StreamObserver;

public class QrResponseStreamObserver implements StreamObserver<QrResponse>{
	
	
	private CountDownLatch latch;
	
	private List<ResponseData> serverResponses;
	public List<ResponseData> getServerResponses() {
		return serverResponses;
	}
	
	
	public QrResponseStreamObserver(CountDownLatch latch){
		this.latch = latch;
		this.serverResponses = new ArrayList<>();
	}

	@Override
	public void onNext(QrResponse qrResponse) {
		LogUtil.logInfo(this.getClass(),"gRPC client  received data from gRPC server - {}",qrResponse);
		this.serverResponses.add(new ResponseData(qrResponse.getQr(),qrResponse.getStatus()));
	}

	@Override
	public void onError(Throwable t) {
		this.latch.countDown();
	}

	@Override
	public void onCompleted() {	
		this.latch.countDown();
		
	}

}
