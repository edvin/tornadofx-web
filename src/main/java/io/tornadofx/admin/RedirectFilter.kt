package io.tornadofx.admin

import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter("/*")
class RedirectFilter : Filter {
    override fun destroy() {
    }

    override fun doFilter(req: ServletRequest, resp: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val response = resp as HttpServletResponse

        when (request.requestURI) {
            "/guide" -> response.sendRedirect("https://www.gitbook.com/book/edvin/tornadofx-guide/details")
            "/slack" -> response.sendRedirect("https://kotlinlang.slack.com/messages/tornadofx/")
            "/screencasts" -> response.sendRedirect("https://youtube.com/mredvinsyse")
            "/github" -> response.sendRedirect("https://github.com/edvin/tornadofx")
            else -> chain.doFilter(request, response)
        }
    }

    override fun init(filterConfig: FilterConfig) {
    }

}

