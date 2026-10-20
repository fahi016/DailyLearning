package com.example.rediscache.ratelimit;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateLimiterService {

    private static final int LIMIT = 5;
    private static final int WINDOW_SECONDS = 60;

    private final StringRedisTemplate redisTemplate;

    private final DefaultRedisScript<Long> script;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;

        this.script = new DefaultRedisScript<>();
        this.script.setScriptText("""
            local count = redis.call('INCR', KEYS[1])
            if count == 1 then
                redis.call('EXPIRE', KEYS[1], ARGV[1])
            end
            return count
            """);
        this.script.setResultType(Long.class);
    }

    public boolean isAllowed(String clientId) {
        long window = System.currentTimeMillis() / 60_000;

        String key = "rate_limit:" + clientId + ":" + window;

        Long count = redisTemplate.execute(
                script,
                List.of(key),
                String.valueOf(WINDOW_SECONDS)
        );

        return count != null && count <= LIMIT;
    }
}
