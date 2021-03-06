package org.sakaiproject.lti.entityprovider;

import org.junit.Before;
import org.junit.Test;
import org.sakaiproject.lti.api.LTIService;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.sakaiproject.lti.api.LTIService.*;

public class LTIEntityProviderTest {

    private LTIEntityProvider provider;
    private Map<String, Object> map;

    @Before
    public void setUp() {
        provider = new LTIEntityProvider();
        map = new HashMap<>();
    }

    @Test
    public void testAdjustMapNoId() {
        provider.adjustMap(map, true, "siteId", "kind");
        assertThat(map.entrySet(), empty());
    }

    @Test
    public void testAdjustMapIsAdmin() {
        map.put(LTIService.LTI_ID, "1");
        map.put(LTI_SECRET, "secret");
        provider.adjustMap(map, true, "siteId", "kind");
        assertThat(map, not(hasEntry(LTI_SECRET, "secret")));
    }

    @Test
    public void testAdjustMapIsNotAdmin() {
        map.put(LTIService.LTI_ID, "1");
        map.put(LTI_SECRET, "secret");
        map.put(LTI_REG_PASSWORD, "password");
        provider.adjustMap(map, false, "siteId", "kind");
        assertThat(map, not(hasEntry(LTI_SECRET, "secret")));
        assertThat(map, not(hasEntry(LTI_REG_PASSWORD, "password")));
    }

    @Test
    public void testAdjustMapAddId() {
        map.put(LTIService.LTI_ID, "1");
        provider.adjustMap(map, true, "siteId", "kind");
        assertThat(map, hasEntry("@id", "/lti/kind/siteId/1.json"));
    }

    @Test
    public void testAdjustMapAllow() {
        map.put(LTIService.LTI_ALLOWSECRET, "1");
        provider.adjustMap(map, false, "siteId", "kind");
        assertThat(map, hasEntry(LTI_ALLOWSECRET, "1"));
    }
}
