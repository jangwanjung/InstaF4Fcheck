package insta.instaf4fcheck.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import insta.instaf4fcheck.model.InstaId;
import jakarta.servlet.http.Cookie;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    @Value("${key}")
    private String key;

    @PostMapping("/check")
    public String check(@RequestParam String username) throws UnsupportedEncodingException, URISyntaxException {
        String url1 = "https://www.instagram.com/web/search/topsearch/?query=" + username;
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
                    instaId = user.path("pk").asText();
                    System.out.println("Username: " + foundUsername + ", ID: " + instaId);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> followersList = new ArrayList<>();
        String endCursor = null;
        boolean hasNextPage = true;

        while (hasNextPage) {
            String variables = String.format("{\"id\":\"%s\",\"include_reel\":true,\"fetch_mutual\":true,\"first\":50,\"after\":\"%s\"}", instaId, endCursor != null ? endCursor : "");
            String encodedVariables = URLEncoder.encode(variables, "UTF-8");
            String url2 = String.format("https://www.instagram.com/graphql/query/?query_hash=c76146de99bb02f6415203be841dd25a&variables=%s", encodedVariables);
            URI uri2 = new URI(url2);
            RestTemplate rt2 = new RestTemplate();
            HttpHeaders headers2 = new HttpHeaders();
            headers2.add("Cookie", key);

            HttpEntity<String> entity2 = new HttpEntity<>(headers2);
            ResponseEntity<String> response2 = rt2.exchange(uri2, HttpMethod.GET, entity2, String.class);
            String responseBody2 = response2.getBody();
            try {
                JsonNode rootNode = objectMapper.readTree(responseBody2);
                JsonNode usersNode = rootNode.path("data").path("user").path("edge_followed_by").path("edges");
                JsonNode pageInfoNode = rootNode.path("data").path("user").path("edge_followed_by").path("page_info");
                hasNextPage = pageInfoNode.path("has_next_page").asBoolean();
                endCursor = pageInfoNode.path("end_cursor").asText();

                for (JsonNode userNode : usersNode) {
                    JsonNode user = userNode.path("node");
                    String userName = user.path("username").asText();
                    followersList.add(userName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("팔로워리스트"+followersList);
        System.out.println("팔로워수: "+followersList.size());

        List<String> followingList = new ArrayList<>();
        endCursor = null;
        hasNextPage = true;

        while (hasNextPage) {
            String variables = String.format("{\"id\":\"%s\",\"include_reel\":true,\"fetch_mutual\":true,\"first\":50,\"after\":\"%s\"}", instaId, endCursor != null ? endCursor : "");
            String encodedVariables = URLEncoder.encode(variables, "UTF-8");
            String url3 = String.format("https://www.instagram.com/graphql/query/?query_hash=d04b0a864b4b54837c0d870b0e77e076&variables=%s", encodedVariables);
            URI uri3 = new URI(url3);
            RestTemplate rt3 = new RestTemplate();
            HttpHeaders headers3 = new HttpHeaders();
            headers3.add("Cookie", key);

            HttpEntity<String> entity3 = new HttpEntity<>(headers3);
            ResponseEntity<String> response3 = rt3.exchange(uri3, HttpMethod.GET, entity3, String.class);
            String responseBody3 = response3.getBody();
            try {
                JsonNode rootNode = objectMapper.readTree(responseBody3);
                JsonNode usersNode = rootNode.path("data").path("user").path("edge_follow").path("edges");
                JsonNode pageInfoNode = rootNode.path("data").path("user").path("edge_follow").path("page_info");
                hasNextPage = pageInfoNode.path("has_next_page").asBoolean();
                endCursor = pageInfoNode.path("end_cursor").asText();

                for (JsonNode userNode : usersNode) {
                    JsonNode user = userNode.path("node");
                    String userName = user.path("username").asText();
                    followingList.add(userName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        System.out.println("팔로잉리스트"+followingList);
        System.out.println("팔로잉수: "+followingList.size());

        List<String> dontFollowMeBack = new ArrayList<>();
        for (String following : followingList) {
            if (!followersList.contains(following)) {
                dontFollowMeBack.add(following);
            }
        }

        List<String> iDontFollowBack = new ArrayList<>();
        for (String follower : followersList) {
            if (!followingList.contains(follower)) {
                iDontFollowBack.add(follower);
            }
        }

        System.out.println("나를 팔로우하지 않는 사람들: " + dontFollowMeBack);
        System.out.println("팔로우하지 않은 사람들: " + iDontFollowBack);

        return "팔로워수 : "+followersList.size()+"<br>"+"팔로잉수 : "+followingList.size()+"<br>"+
                "내가 팔로우를 안한사람수 : "+iDontFollowBack.size()+"<br>"+"나를 팔로우 안한사람 수 : "+dontFollowMeBack.size()+"<br>"+
                "내가 팔로우를 안한사람 : "+iDontFollowBack+"<br>"+"나를 팔로우 안한사람 : "+dontFollowMeBack;
    }
}
