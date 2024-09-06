package insta.instaf4fcheck.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import insta.instaf4fcheck.model.InstaId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
public class Controller {

    @Value("${key}")
    private String key;

    @PostMapping("/check")
    public String check(@RequestParam String username) throws UnsupportedEncodingException {
        String url1 = "https://www.instagram.com/web/search/topsearch/?query="+username;
        RestTemplate rt1 = new RestTemplate();
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Cookie", key);
        HttpEntity<String> entity = new HttpEntity<>(headers1);
        ResponseEntity<String> response = rt1.exchange(url1, HttpMethod.GET, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = response.getBody();
        String instaId = null;
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode usersNode = rootNode.path("users");

            for (JsonNode userNode : usersNode) {
                JsonNode user = userNode.path("user");
                String foundUsername = user.path("username").asText();
                if (foundUsername.equals(username)) {
                    instaId = user.path("id").asText();
                    System.out.println("Username: " + foundUsername + ", ID: " + instaId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String variables = String.format("{\"id\":\"%s\",\"include_reel\":true,\"fetch_mutual\":true,\"first\":50,\"after\":null}", instaId);
        String encodedVariables = URLEncoder.encode(variables, "UTF-8");
        String url2 = String.format("https://www.instagram.com/graphql/query/?query_hash=c76146de99bb02f6415203be841dd25a&variables=%s", encodedVariables);
        System.out.println(url2);

        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Cookie", key);
        HttpEntity<String> entity2 = new HttpEntity<>(headers2);
        ResponseEntity<String> response2 = rt2.exchange(url2, HttpMethod.GET, entity2, String.class);
        System.out.println(response2.getBody());

        return response2.getBody();
    }
}
