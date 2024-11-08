package com.example.demo.service;
import com.example.demo.model.Job;
import com.example.demo.repository.JobRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JobService {

    private JobRepository jobRepository;
    private final RestTemplate restTemplate;

    @Value("${coresignal.api.url}")
    private String apiUrl;

    @Value("${coresignal.api.url.collect}")
    private String collectUrl;

    @Value("${coresignal.api.token}")
    private String apiToken;


    @Autowired
    public JobService(RestTemplate restTemplate, JobRepository jobRepository) {
        this.jobRepository = jobRepository;
        this.restTemplate = restTemplate;
    }

    // Save a job to the database
    public Job addJob(Job job) {
        return jobRepository.save(job);
    }

    // Get a job by ID
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public List<Map<String, String>> searchJobs(String keywordDescription, String location) {
        // Step 1: Fetch the list of job IDs
        List<Integer> jobIds = fetchJobs(keywordDescription, location);

        // Step 2: Fetch job details for each job ID
        List<Map<String, String>> jobs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int jobId = jobIds.get(i);
            Map<String, String> jobDetails = fetchJobDetails(jobId);
            if (jobDetails != null) {
                jobs.add(jobDetails);
            }
        }
        return jobs;
    }

    private List<Integer> fetchJobs(String keywordDescription, String location) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiToken);

        JSONObject requestBody = new JSONObject();
        requestBody.put("keyword_description", keywordDescription);
        requestBody.put("location", location);
        requestBody.put("application_active", true);
        requestBody.put("country", "United States");

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );


        String response = responseEntity.getBody();
        List<Integer> jobIds = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            jobIds.add(jsonArray.getInt(i));
        }
        return jobIds;
    }

    private Map<String, String> fetchJobDetails(Integer jobId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = collectUrl.replace("{job_id}", jobId.toString());

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JSONObject jobJson = new JSONObject(responseEntity.getBody());
            return Map.of(
                    "title", jobJson.optString("title", "N/A"),
                    "company", jobJson.optString("company_name", "N/A"),
                    "description", jobJson.optString("description", "N/A"),
                    "url", jobJson.optString("redirected_url", "N/A")
            );
        } catch (Exception e) {
            System.err.println("Failed to fetch job details for job ID " + jobId + ": " + e.getMessage());
            return null;
        }
    }

}
