package jwtexample.backenduser.security

import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthorizationFilter : BasicAuthenticationFilter {

    private var jwtUtil: JWTUtil
    private var userDetailService: UserDetailsService
    private val requestMatcher: RequestMatcher = AntPathRequestMatcher("/user", HttpMethod.POST.toString())

    constructor(authenticationManager: AuthenticationManager, jwtUtil: JWTUtil, userDetailService: UserDetailsService) : super(authenticationManager) {
        this.jwtUtil = jwtUtil
        this.userDetailService = userDetailService
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (this.requestMatcher.matches(request)) {
            chain.doFilter(request, response)
            return
        }

        val authorizationHeader = request.getHeader("authorization")
        if(authorizationHeader.startsWith("bearer")) {
            val auth = getAuthentication(authorizationHeader)
            SecurityContextHolder.getContext().authentication = auth
        }

        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorizationHeader: String?): UsernamePasswordAuthenticationToken {
        val token = authorizationHeader?.substring(7) ?: ""
        if(jwtUtil.isTokenValid(token)) {
            val username = jwtUtil.getUserName(token)
            val user = userDetailService.loadUserByUsername(username)
            return UsernamePasswordAuthenticationToken(user, null, user.authorities)
        }
        throw UsernameNotFoundException("Auth invalid!")
    }

}