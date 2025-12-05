package io.gridbug.exif_std;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.CqListener;
import org.apache.geode.cache.query.CqQuery;
import org.apache.geode.cache.query.CqAttributes;
import org.apache.geode.cache.query.CqAttributesFactory;
import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.cache.Operation;

import java.io.File;
import java.io.IOException;

public class ExecuteContinuousQuery implements CqListener {
    public static void main(String[] args) {
        try {
            ClientCache cache;
            Region<String, PhotoExifData> region;

            cache = new ClientCacheFactory().addPoolLocator("10.88.88.123", 10334)
                    .setPdxSerializer(new ReflectionBasedAutoSerializer(
                            true,
                            "io.gridbug.exif_std.PhotoExifData"))
                    .setPoolSubscriptionEnabled(true)
                    .create();

            region = cache.<String, PhotoExifData>createClientRegionFactory(ClientRegionShortcut.PROXY)
                    .create("PhotoExifData");

            QueryService queryService = cache.getQueryService();

            CqAttributesFactory cqAttributesFactory = new CqAttributesFactory();
            cqAttributesFactory.addCqListener(new ExecuteContinuousQuery());
            CqAttributes cqAttributes = cqAttributesFactory.create();
            String query = "SELECT * FROM /PhotoExifData p WHERE p.model = 'Pixel 8 Pro';";
            CqQuery cqQuery = queryService.newCq("pixel 8 pro photos", query, cqAttributes);
            cqQuery.execute();

            System.out.println("continuous query running...");
            while (true) {
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(CqEvent event) {
        // System.out.println("continuous query event: " + event);
        Object key = event.getKey();
        PhotoExifData value = (PhotoExifData) event.getNewValue();

        Operation op = event.getQueryOperation();
        if (op.isCreate()) {
            System.out.println("created: " + key + ", value: " + value);
        } else if (op.isUpdate()) {
            System.out.println("updated: " + key + ", value: " + value);
        } else if (op.isDestroy()) {
            System.out.println("destroyed: " + key + ", value: " + value);
        }
    }

    @Override
    public void onError(CqEvent event) {
        System.out.println("Error: " + event);

    }

    @Override
    public void close() {
        System.out.println("closing continuous query");
    }

}
