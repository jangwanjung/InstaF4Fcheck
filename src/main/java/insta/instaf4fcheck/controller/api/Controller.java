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
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Value("${key}")
    private String key;

    @PostMapping("/check")
    public String check(@RequestParam String username, Model model) throws UnsupportedEncodingException, URISyntaxException {
        String url1 = "https://www.instagram.com/web/search/topsearch/?query=" + username;
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", key);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        long startTime = System.currentTimeMillis();
        ResponseEntity<String> response = rt.exchange(url1, HttpMethod.GET, entity, String.class);
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
                    if(user.path("is_private").asText().equals("true")){
                        return "비공개계정";
                    }
                    break;
                }
            }
            if(instaId == null){
                return "못찾음";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> followersList = new ArrayList<>();
        HashMap<String, String> followersMap = new HashMap<>();
        String endCursor = null;
        boolean hasNextPage = true;
        while (hasNextPage) {
            String variables = String.format("{\"id\":\"%s\",\"include_reel\":true,\"fetch_mutual\":true,\"first\":50,\"after\":\"%s\"}", instaId, endCursor != null ? endCursor : "");
            String encodedVariables = URLEncoder.encode(variables, "UTF-8");
            String url2 = String.format("https://www.instagram.com/graphql/query/?query_hash=c76146de99bb02f6415203be841dd25a&variables=%s", encodedVariables);
            URI uri2 = new URI(url2);

            ResponseEntity<String> response2 = rt.exchange(uri2, HttpMethod.GET, entity, String.class);

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
                    String fullName = user.path("full_name").asText();
                    String profileUrl = user.path("profile_pic_url").asText();
                    followersList.add(userName);
                    followersMap.put(userName, fullName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> followingList = new ArrayList<>();
        HashMap<String, String> followingMap = new HashMap<>();
        endCursor = null;
        hasNextPage = true;

        while (hasNextPage) {
            String variables = String.format("{\"id\":\"%s\",\"include_reel\":true,\"fetch_mutual\":true,\"first\":50,\"after\":\"%s\"}", instaId, endCursor != null ? endCursor : "");
            String encodedVariables = URLEncoder.encode(variables, "UTF-8");
            String url3 = String.format("https://www.instagram.com/graphql/query/?query_hash=d04b0a864b4b54837c0d870b0e77e076&variables=%s", encodedVariables);
            URI uri3 = new URI(url3);

            ResponseEntity<String> response3 = rt.exchange(uri3, HttpMethod.GET, entity, String.class);
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
                    String fullName = user.path("full_name").asText();
                    String profileUrl = user.path("profile_pic_url").asText();
                    followingList.add(userName);
                    followingMap.put(userName, fullName);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


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

        long endTime = System.currentTimeMillis(); // 종료 시간 측정
        long duration = endTime - startTime; // 걸린 시간 계산
        System.out.println("시간 : " + duration + " 밀리초");
        model.addAttribute("dontFollowMeBack",dontFollowMeBack);
        model.addAttribute("iDontFollowBack",iDontFollowBack);
        model.addAttribute("followersMap",followersMap);
        model.addAttribute("followingMap",followingMap);



        return "result";
    }
}


