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

import java.net.InetSocketAddress;

import java.util.ArrayList;
import java.util.List;

import com.addthis.metrics.reporter.config.AbstractHttpReporterConfig;
import com.addthis.metrics.reporter.config.HostPort;
import com.codahale.metrics.MetricRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpReporterConfig extends AbstractHttpReporterConfig implements MetricsReporterConfigThree
{
    private static final Logger log = LoggerFactory.getLogger(HttpReporterConfig.class);

    private List<HttpReporter> reporters;

    @Override
    public boolean enable(MetricRegistry registry)
    {
        reporters = new ArrayList<HttpReporter>();

        for (HostPort hostPort: getFullHostList())
        {
            HttpReporter reporter =
                    HttpReporter.forRegistry(registry)
                                .setHost(hostPort.getHost())
                                .setPort(hostPort.getPort())
                                .build();

            log.debug("Activating HttpReporterConfig for host {}:{}", hostPort.getHost(), hostPort.getPort());
            reporters.add(reporter);
        }

        return true;
    }

    @Override
    public void report() {
        for (HttpReporter reporter: reporters)
        {
            reporter.report();
        }
    }

}
