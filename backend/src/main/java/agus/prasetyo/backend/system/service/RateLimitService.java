package agus.prasetyo.backend.system.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    @Value("${server.login.attemp}")
    private int loginAttemp;

    @Value("${server.login.attemp.in.minutes}")
    private int loginAttempInMinutes;

    @Value("${server.login.block.minutes}")
    private int loginBlockMinutes;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final Map<String, Instant> blockedClients = new ConcurrentHashMap<>();
    private final Duration blockDuration = Duration.ofMinutes(loginBlockMinutes);

    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, this::newBucket);
    }

    private Bucket newBucket(String key) {
        Bandwidth limit = Bandwidth.classic(loginAttemp, Refill.intervally(loginAttemp, Duration.ofMinutes(loginAttempInMinutes)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    public boolean isBlocked(String key) {
        if (blockedClients.containsKey(key)) {
            Instant blockedUntil = blockedClients.get(key);
            if (blockedUntil.isAfter(Instant.now())) {
                return true;
            } else {
                blockedClients.remove(key);
            }
        }
        return false;
    }

    public void blockClient(String key) {
        blockedClients.put(key, Instant.now().plus(blockDuration));
    }
}

