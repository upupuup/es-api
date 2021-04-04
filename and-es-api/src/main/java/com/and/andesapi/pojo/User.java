package com.and.andesapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/4/4 10:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {
	private String name;
	private int age;
}
