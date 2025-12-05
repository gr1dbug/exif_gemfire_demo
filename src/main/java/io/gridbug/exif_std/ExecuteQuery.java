package io.gridbug.exif_std;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;

import java.io.File;
import java.io.IOException;

public class ExecuteQuery {
	public static void main(String[] args) {
		try {
			ClientCache cache;
			Region<String, PhotoExifData> region;

			cache = new ClientCacheFactory().addPoolLocator("10.88.88.123", 10334)
					.setPdxSerializer(new ReflectionBasedAutoSerializer(
							true,
							"io.gridbug.exif_std.PhotoExifData"))
					.create();

			region = cache.<String, PhotoExifData>createClientRegionFactory(ClientRegionShortcut.PROXY)
					.create("PhotoExifData");

			QueryService queryService = cache.getQueryService();
			Query query = queryService.newQuery("SELECT * FROM /PhotoExifData");
			SelectResults<PhotoExifData> results = (SelectResults<PhotoExifData>) query.execute();

			for (PhotoExifData result : results) {
				System.out.println(result);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
