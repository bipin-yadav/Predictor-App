
package com.peak.predictor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseResourceController {

  @RequestMapping(value = {"/"})
  public String index() {
    return "index.html";
  }

  @RequestMapping(value = {"/dashboard"})
  public String dashboard() {
    return "index.html";
  }

}
