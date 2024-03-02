### Guides

The following guides illustrate how to run the project:

1. **Navigate to the FrontEnd directory:**

   ```bash
   cd FrontEnd
   ```

````

2. **Build the React project:**

   ```bash
   npm run build
   ```

3. **Copy the build files to the Spring Boot static directory:**

   ```bash
   cp -r build/* ../src/main/resources/static
   ```

4. **Navigate back to the project root directory:**

   ```bash
   cd ..
   ```

5. **Run the Spring Boot application:**

   ```bash
   mvn spring-boot:run
   ```

````
