package com.gmail.burinigor7.userscrudservice.api;

import com.gmail.burinigor7.userscrudservice.exception.AdminDeletionServiceNotAccessibleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminDeletionApiTests {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AdminDeletionApi adminDeletionApi;

    @Test
    public void isAllowed_whenNotGrantedAdmin_updateCacheAndReturnFalse() throws Exception {
        long checkedId = 10L;
        Set<Long> cache = adminDeletionApi.getCache();
        cache.add(checkedId);
        when(restTemplate.getForObject(null, boolean.class, checkedId))
                .thenReturn(false);
        assertFalse(adminDeletionApi.isAllowed(checkedId));
        assertEquals(0, cache.size());
    }

    @Test
    public void isAllowed_whenGrantedAdmin_updateCacheAndReturnTrue() throws Exception {
        long checkedId = 10L;
        Set<Long> cache = adminDeletionApi.getCache();
        when(restTemplate.getForObject(null, boolean.class, checkedId))
                .thenReturn(true);
        assertTrue(adminDeletionApi.isAllowed(checkedId));
        assertEquals(1, cache.size());
    }

    @Test
    public void isAllowedRecover_whenCacheContains_returnTrue() throws Exception {
        long checkedId = 10L;
        Set<Long> cache = adminDeletionApi.getCache();
        cache.add(checkedId);
        assertTrue(adminDeletionApi.isAllowedRecover(null,
                null, checkedId));
    }

    @Test
    public void isAllowedRecover_whenCacheNotContains_throwsException() throws Exception {
        long checkedId = 10L;
        assertThrows(AdminDeletionServiceNotAccessibleException.class,
                () -> adminDeletionApi.isAllowedRecover(null, null, checkedId));
    }
}
