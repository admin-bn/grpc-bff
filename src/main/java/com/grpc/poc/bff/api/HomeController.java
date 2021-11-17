package com.grpc.poc.bff.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grpc.poc.bff.dto.ResponseData;
import com.grpc.poc.bff.service.QrClientService;

@RestController
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private QrClientService service;
	
	@Value("${grpc.client.grpc-poc-backend-service.address}")
	private String serverEndpoint;

	@GetMapping()
	public ResponseEntity<Map<String, Object>> index() throws InterruptedException {
		String grpcRequest = "{\"data\":\"ping\",\"status\":\"ping\"}";
		List<ResponseData> dataFromGrpcServer = service.getstreamingQrBySessionIdAsync(grpcRequest);
		Map<String, Object> map = new HashMap<>();
		map.put("name", "grpc-poc-bff-service");
		map.put("version", "1");
		map.put("listening to gRPC server at ", serverEndpoint);
		map.put("gRPC data send from client ", grpcRequest);
		map.put("gRPC data received from server ", dataFromGrpcServer.get(0));
		return ResponseEntity.status(HttpStatus.OK).body(map);
	}
	
	@GetMapping("hello")
	public ResponseEntity<List<ResponseData>> hello() throws InterruptedException, JsonProcessingException {
		
		
		List<ResponseData> dataFromGrpcServer = service.getstreamingQrBySessionIdAsync("{\"data\":\"ping\",\"status\":\"ping\"}");
		
		return ResponseEntity.status(HttpStatus.OK).body(dataFromGrpcServer);
	}

        @GetMapping("test")
        public ResponseEntity<String> test() {
                return ResponseEntity.status(HttpStatus.OK).body("non gRpc call...");
        }

}
