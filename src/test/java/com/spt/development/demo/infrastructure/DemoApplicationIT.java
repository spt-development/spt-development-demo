package com.spt.development.demo.infrastructure;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/spt/development/demo/infrastructure/cucumber")
class DemoApplicationIT {
}
