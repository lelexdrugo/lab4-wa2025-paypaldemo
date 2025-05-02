package tonicminds.paypaldemo

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController {

    @GetMapping("/", "/order/**")
    fun forwardReactRoutes(): String {
        return "forward:/index.html"
    }
}