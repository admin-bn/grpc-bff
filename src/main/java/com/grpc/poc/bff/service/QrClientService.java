package com.grpc.poc.bff.service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grpc.poc.bff.LogUtil;
import com.grpc.poc.bff.dto.ResponseData;
import com.grpc.poc.bff.transferstream.QrResponseStreamObserver;
import com.grpc.poc.proto.dto.qr.QrRequest;
import com.grpc.poc.proto.dto.qr.QrServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class QrClientService {

	@GrpcClient("grpc-poc-backend-service")
	private QrServiceGrpc.QrServiceStub asyncStub;

	public List<ResponseData> getstreamingQrBySessionIdAsync(String sessionId) throws InterruptedException {

		CountDownLatch latch = new CountDownLatch(1);

		QrResponseStreamObserver responseStreamObserver = new QrResponseStreamObserver(latch);

		try {

			StreamObserver<QrRequest> requestStreamObserver = this.asyncStub.getQrAsync(responseStreamObserver);

			ObjectMapper mapper = new ObjectMapper();
			ResponseData responseData = mapper.readValue(sessionId, ResponseData.class);

			QrRequest request = QrRequest.newBuilder().setSession(responseData.getData())
					.setStatus(responseData.getStatus()).build();
			requestStreamObserver.onNext(request);

			requestStreamObserver.onCompleted();

			latch.await();
			
			LogUtil.logInfo(this.getClass(),"gRPC client send the data -{} to the server",request);

		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logInfo(this.getClass(),"Exception, {}",e);
		}
		return responseStreamObserver.getServerResponses();
	}

}
