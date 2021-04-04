package com.and.andesjd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/4/4 14:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Content {
	private String title;
	private String img;
	private String price;
}
