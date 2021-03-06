/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.addthis.metrics3.reporter.config;

import javax.validation.Valid;

import java.io.IOException;

import java.lang.management.ManagementFactory;
import java.util.List;

import com.addthis.metrics.reporter.config.AbstractReporterConfig;

import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;

import com.codahale.metrics.jvm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// ser/d class.  May make a different abstract for commonalities

// Stupid bean for simplicity and snakeyaml, instead of @Immutable
// like any sane person would intend
public class ReporterConfig extends AbstractReporterConfig {
    private static final Logger log = LoggerFactory.getLogger(ReporterConfig.class);

    @Valid
    private List<ConsoleReporterConfig> console;
    @Valid
    private List<CsvReporterConfig> csv;
    @Valid
    private List<GangliaReporterConfig> ganglia;
    @Valid
    private List<GraphiteReporterConfig> graphite;
    @Valid
    private List<InfluxDBReporterConfig> influxdb;
    @Valid
    private List<RiemannReporterConfig> riemann;
    @Valid
    private List<StatsDReporterConfig> statsd;
    @Valid
    private List<ZabbixReporterConfig> zabbix;
    @Valid
    private List<PrometheusReporterConfig> prometheus;
    @Valid
    private List<HttpReporterConfig> http;

    private boolean jvmMetricsEnabled = false;

    public List<ConsoleReporterConfig> getConsole() {
        return console;
    }

    public void setConsole(List<ConsoleReporterConfig> console) {
        this.console = console;
    }

    public List<CsvReporterConfig> getCsv() {
        return csv;
    }

    public void setCsv(List<CsvReporterConfig> csv) {
        this.csv = csv;
    }

    public List<GangliaReporterConfig> getGanglia() {
        return ganglia;
    }

    public void setGanglia(List<GangliaReporterConfig> ganglia) {
        this.ganglia = ganglia;
    }

    public List<GraphiteReporterConfig> getGraphite() {
        return graphite;
    }

    public void setGraphite(List<GraphiteReporterConfig> graphite) {
        this.graphite = graphite;
    }

    public List<InfluxDBReporterConfig> getInfluxdb() {
        return influxdb;
    }

    public void setInfluxdb(List<InfluxDBReporterConfig> influxdb) {
        this.influxdb = influxdb;
    }

    public List<RiemannReporterConfig> getRiemann() {
        return riemann;
    }

    public void setRiemann(List<RiemannReporterConfig> riemann) {
        this.riemann = riemann;
    }

    public void setHttp(List<HttpReporterConfig> http) {
        this.http = http;
    }

    public List<HttpReporterConfig> getHttp() {
        return this.http;
    }

    public List<StatsDReporterConfig> getStatsd() {
        return statsd;
    }

    public void setStatsd(List<StatsDReporterConfig> statsd) {
        this.statsd = statsd;
    }

    public List<ZabbixReporterConfig> getZabbix() {
        return zabbix;
    }

    public void setZabbix(List<ZabbixReporterConfig> zabbix) {
        this.zabbix = zabbix;
    }

    public void setPrometheus(List<PrometheusReporterConfig> prometheus) {
        this.prometheus = prometheus;
    }

    public List<PrometheusReporterConfig> getPrometheus() {
        return this.prometheus;
    }

    public boolean isJvmMetricsEnabled() {
        return jvmMetricsEnabled;
    }

    public void setJvmMetricsEnabled(boolean jvmMetricsEnabled) {
        this.jvmMetricsEnabled = jvmMetricsEnabled;
    }

    public boolean enableConsole(MetricRegistry registry) {
        boolean failures = false;
        if (console == null) {
            log.debug("Asked to enable console, but it was not configured");
            return false;
        }
        for (ConsoleReporterConfig consoleConfig : console) {
            if (!consoleConfig.enable(registry)) {
                failures = true;
            }
        }
        return !failures;
    }

    public boolean enableCsv(MetricRegistry registry) {
        boolean failures = false;
        if (csv == null) {
            log.debug("Asked to enable csv, but it was not configured");
            return false;
        }
        for (CsvReporterConfig csvConfig : csv) {
            if (!csvConfig.enable(registry)) {
                failures = true;
            }
        }
        return !failures;
    }

    public boolean enableGanglia(MetricRegistry registry) {
        boolean failures = false;
        if (ganglia == null) {
            log.debug("Asked to enable ganglia, but it was not configured");
            return false;
        }
        for (GangliaReporterConfig gangliaConfig : ganglia) {
            if (!gangliaConfig.enable(registry)) {
                failures = true;
            }
        }
        return !failures;
    }

    public boolean enableGraphite(MetricRegistry registry) {
        boolean failures = false;
        if (graphite == null) {
            log.debug("Asked to enable graphite, but it was not configured");
            return false;
        }
        for (GraphiteReporterConfig graphiteConfig : graphite) {
            if (!graphiteConfig.enable(registry)) {
                failures = true;
            }
        }
        return !failures;
    }

    public boolean enablePrometheus(MetricRegistry registry) {
        boolean failures = false;

        if (prometheus == null) {
            log.debug("Asked to enable prometheus, but it was not configured");
            return false;
        }

        for (PrometheusReporterConfig prometheusConfig : prometheus) {
            if (!prometheusConfig.enable(registry)) {
                failures = true;
            }
        }

        return !failures;
    }

    public boolean enableInfluxdb(MetricRegistry registry) {
        boolean failures = false;
        if (influxdb == null) {
            log.debug("Asked to enable influx, but it was not configured");
            return false;
        }
        for (InfluxDBReporterConfig influxConfig : influxdb) {
            if (!influxConfig.enable(registry)) {
                failures = true;
            }
        }
        return !failures;
    }

    public boolean enableRiemann(MetricRegistry registry) {
        boolean failures = false;
        if (riemann == null) {
            log.debug("Asked to enable riemann, but it was not configured");
            return false;
        }
        for (RiemannReporterConfig riemannConfig : riemann) {
            if (!riemannConfig.enable(registry)) {
                failures = true;
            }
        }
        return !failures;
    }

    public boolean enableStatsd(MetricRegistry registry) {
        boolean failures = false;
        if (statsd == null) {
            log.debug("Asked to enable statsd, but it was not configured");
            return false;
        }
        for (StatsDReporterConfig statsdConfig : statsd) {
            if (!statsdConfig.enable(registry)) {
                failures = true;
            }
        }
        return !failures;
    }

    public boolean enableZabbix(MetricRegistry registry) {
        boolean failures = false;
        if (zabbix == null) {
            log.debug("Asked to enable zabbix, but it was not configured");
            return false;
        }
        for (ZabbixReporterConfig zabbixConfig : zabbix) {
            if (!zabbixConfig.enable(registry)) {
                failures = true;
            }
        }
        return !failures;
    }

    public boolean enableHttp(MetricRegistry registry) {
        boolean failures = false;
        if (http == null) {
            log.debug("Asked to enable http, but it was not configured");
            return false;
        }

        for (HttpReporterConfig httpConfig: http) {
            if (!httpConfig.enable(registry)) {
                failures = false;
            }
        }

        return !failures;
    }

    public boolean enableAll(MetricRegistry registry) {
        boolean enabled = false;
        if (console != null && enableConsole(registry)) {
            enabled = true;
        }
        if (csv != null && enableCsv(registry)) {
            enabled = true;
        }
        if (ganglia != null && enableGanglia(registry)) {
            enabled = true;
        }
        if (graphite != null && enableGraphite(registry)) {
            enabled = true;
        }
        if (influxdb != null && enableInfluxdb(registry)) {
            enabled = true;
        }
        if (riemann != null && enableRiemann(registry)) {
            enabled = true;
        }
        if (statsd != null && enableStatsd(registry)) {
            enabled = true;
        }
        if (zabbix != null && enableZabbix(registry)) {
            enabled = true;
        }
        if (prometheus != null && enablePrometheus(registry)) {
            enabled = true;
        }
        if (http != null && enableHttp(registry)) {
            enabled = true;
        }
        if (!enabled) {
            log.warn("No reporters were succesfully enabled");
        }
        if (jvmMetricsEnabled) {
            registerJvmMetrics(registry);
        }
        return enabled;
    }

    private void report(List<? extends MetricsReporterConfigThree> reporters) {
        if (reporters != null) {
            for (MetricsReporterConfigThree reporter : reporters) {
                reporter.report();
            }
        }
    }

    private void registerJvmMetrics(MetricRegistry registry) {
        registry.register("jvm.attribute", new JvmAttributeGaugeSet());
        registry.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
        registry.register("jvm.classloader", new ClassLoadingGaugeSet());
        registry.register("jvm.filedescriptor", new FileDescriptorRatioGauge());
        registry.register("jvm.gc", new GarbageCollectorMetricSet());
        registry.register("jvm.memory", new MemoryUsageGaugeSet());
        registry.register("jvm.threads", new ThreadStatesGaugeSet());
    }

    @SuppressWarnings("unused")
    public void report() {
        report(console);
        report(csv);
        report(ganglia);
        report(graphite);
        report(influxdb);
        report(riemann);
        report(zabbix);
        report(prometheus);
        report(http);
    }

    public static ReporterConfig loadFromFileAndValidate(String fileName) throws IOException {
        ReporterConfig config = loadFromFile(fileName);
        if (validate(config)) {
            return config;
        } else {
            throw new ReporterConfigurationException("configuration failed validation");
        }
    }

    public static ReporterConfig loadFromFile(String fileName) throws IOException {
        return AbstractReporterConfig.loadFromFile(fileName, ReporterConfig.class);
    }

}
