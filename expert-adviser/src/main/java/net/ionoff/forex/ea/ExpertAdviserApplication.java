package net.ionoff.forex.ea;

import net.ionoff.forex.ea.stratergy.BinaryDecisionConfig;
import net.ionoff.forex.ea.stratergy.BinaryDecisionTree;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@SpringBootApplication
public class ExpertAdviserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpertAdviserApplication.class, args);
	}

	@Bean
	@Qualifier("binaryDecisionTreeList")
	public List<BinaryDecisionTree> binaryDecisionTreeList() {
		return BinaryDecisionConfig.createDecisionTrees();
	}
}
