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

package com.addthis.metrics.reporter.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractHttpReporterConfig extends AbstractHostPortReporterConfig
{
    private static final Logger log = LoggerFactory.getLogger(AbstractStatsDReporterConfig.class);

    // @Improvement
    //      Can this be automatically discovered through the Snitch instead of being configured manually ?

    protected String datacenter;

    protected String hostname;

    @Override
    public List<HostPort> getFullHostList()
    {
        return getHostListAndStringList();
    }

    public String getDatacenter()
    {
        return this.datacenter;
    }

    public void setDatacenter(String datacenter)
    {
        this.datacenter = datacenter;
    }

    public String getHostname()
    {
        return this.hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    protected boolean setup(String className)
    {
        List<HostPort> hosts = getFullHostList();
        if (hosts == null || hosts.isEmpty())
        {
            log.error("No hosts specified, cannot enable StatsD Reporter");
            return false;
        }
        return true;
    }
}
