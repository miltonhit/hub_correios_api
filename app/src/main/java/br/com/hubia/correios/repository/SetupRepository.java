package br.com.hubia.correios.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import br.com.hubia.correios.model.Address;

@Repository
public class SetupRepository {

	@Value("${setup.origin.url}")
	private String completeUrl;

	public List<Address> listAdressesFromOrigin() throws Exception {
		List<Address> result = new ArrayList<>();
		String resultStr = null;
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(new HttpGet(completeUrl))) {

			HttpEntity entity = response.getEntity();
			resultStr = EntityUtils.toString(entity);
		}

		for (String current : resultStr.split("\n")) {
			String[] currentSplited = current.split(",");

			if (currentSplited[0].length() > 2) // breaks the header line, if exists
				continue;

			result.add(Address.builder()
					.state(currentSplited[0])
					.city(currentSplited[1])
					.district(currentSplited[2])
					.zipcode(StringUtils.leftPad(currentSplited[3], 8, "0"))
					.street(currentSplited.length > 4 ? currentSplited[4] : null).build());
		}

		return result;
	}
}