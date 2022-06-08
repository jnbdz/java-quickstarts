# Singleton Java Example

When running `SingletonApplication->main()` (Spring Boot example) you will get an HTTP server running on port 8080.

You can access it here: http://localhost:8080/

You can try the different requests:
- http://localhost:8080/echo
- http://localhost:8080/print
- http://localhost:8080/second/echo
- http://localhost:8080/second/print

If you look at the logs you will see that the `@Autowired Data data;` is always the same... Meaning it's a singleton.

Example: 
```
com.example.singleton.repositories.Data@3eeed942
com.example.singleton.repositories.Data@3eeed942
com.example.singleton.repositories.Data@3eeed942
com.example.singleton.repositories.Data@3eeed942
```

For Java without Spring Boot you can run: `DemoSingleThread->main()`.

In the terminal you should see the example's result.

The code comes from: https://refactoring.guru/design-patterns/singleton/java/example

## Code explained
- [Spring @Component Annotation | Baeldung](https://www.baeldung.com/spring-component-annotation)
    - `@Component` is used for the singleton in the Spring Boot version
    - It is similar to other annotation like `@Controller`, `@Service`, and `@repository`