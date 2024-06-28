package hack.maze.filter;

import hack.maze.service.UserService;
import hack.maze.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static hack.maze.config.UserContext.clearContext;
import static hack.maze.config.UserContext.setCurrentUser;
import static hack.maze.constant.SecurityConstant.TOKEN_PREFIX;
import static hack.maze.utils.GlobalMethods.isAuthEndpoint;
import static hack.maze.utils.GlobalMethods.isSwaggerEndpoint;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class GetCurrentUserFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            if (!isAuthEndpoint(request) && !isSwaggerEndpoint(request)) {
                String authHeader = request.getHeader(AUTHORIZATION);
                String jwt = authHeader.substring(TOKEN_PREFIX.length());
                long userId = (long) (int) jwtUtils.extractClaims(jwt).get("userId");
                setCurrentUser(userId);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
            clearContext();
        }

    }


}