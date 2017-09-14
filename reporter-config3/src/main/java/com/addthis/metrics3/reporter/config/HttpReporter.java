package com.addthis.metrics3.reporter.config;

import com.codahale.metrics.*;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

public class HttpReporter extends ScheduledReporter {

    private static final Logger log = LoggerFactory.getLogger(HttpReporter.class);

    private HttpClient httpClient;
    private URI uri;
    private final ObjectMapper mapper;

    public static Builder forRegistry(MetricRegistry registry) {
        return new Builder(registry);
    }

    public static class Builder {
        private final MetricRegistry registry;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private MetricFilter filter;

        private String host;
        private int port;

        private String dataCenterName;

        private String hostName;

        private String prefix;

        private Builder(final MetricRegistry registry) {
            this.registry = registry;
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.filter = MetricFilter.ALL;
            this.prefix = "/";
        }

        Builder setRateUnit(TimeUnit rateUnit)
        {
            this.rateUnit = rateUnit;
            return this;
        }

        Builder setDurationUnit(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        Builder setMetricFilter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        Builder setHost(String host) {
            this.host = host;
            return this;
        }

        Builder setPort(int port) {
            this.port = port;
            return this;
        }

        Builder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        Builder setDatacenterName(String dataCenterName) {
            this.dataCenterName = dataCenterName;
            return this;
        }

        Builder setHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        private URI makeUri() {
            try {
                return new URL(String.format("http://%s:%d%s/%s/%s", host, port, prefix, dataCenterName, hostName)).toURI();
            } catch (Exception e) {
                log.error("Could not create URL: {}", e.getMessage());
            }

            return null;
        }

        public HttpReporter build()
        {
            return new HttpReporter(registry, rateUnit, durationUnit, filter, makeUri());
        }
    }

    private HttpReporter(
            MetricRegistry registry, TimeUnit rateUnit, TimeUnit durationUnit, MetricFilter filter,
            URI uri) {
        super(registry, "http-reporter", filter, rateUnit, durationUnit);

        this.httpClient = HttpClientBuilder.create().build();
        this.uri = uri;
        this.mapper = new ObjectMapper().registerModule(
                new MetricsModule(rateUnit, durationUnit, false, filter));

        log.info("Activating HttpReporter for {}", uri.toString());
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges,
                       SortedMap<String, Counter> counters,
                       SortedMap<String, Histogram> histograms,
                       SortedMap<String, Meter> meters,
                       SortedMap<String, Timer> timers) {

        final MetricMessage message = new MetricMessage(gauges, counters, histograms, meters, timers);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            mapper.writeValue(writer, message);

            HttpPost req = new HttpPost(uri);

            StringEntity entity = new StringEntity(out.toString());
            entity.setContentType("application/json");

            req.setEntity(entity);

            httpClient.execute(req);

        } catch (IOException e) {
            log.error("Could not send HTTP request");
            e.printStackTrace();
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class MetricMessage {
        private final long timestamp;
        private final SortedMap<String, Gauge> gauges;
        private final SortedMap<String, Counter> counters;
        private final SortedMap<String, Histogram> histograms;
        private final SortedMap<String, Meter> meters;
        private final SortedMap<String, Timer> timers;

        public MetricMessage(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
                             SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters,
                             SortedMap<String, Timer> timers)
        {
            this.gauges = gauges;
            this.counters = counters;
            this.histograms = histograms;
            this.meters = meters;
            this.timers = timers;
            this.timestamp = System.currentTimeMillis();
        }

        public SortedMap<String, Gauge> getGauges()
        {
            return this.gauges;
        }

        public SortedMap<String, Counter> getCounters()
        {
            return this.counters;
        }

        public SortedMap<String, Histogram> getHistograms()
        {
            return this.histograms;
        }

        public SortedMap<String, Meter> getMeters()
        {
            return this.meters;
        }

        public SortedMap<String, Timer> getTimers()
        {
            return this.timers;
        }

        public long getTimestamp()
        {
            return this.timestamp;
        }

    }

}

