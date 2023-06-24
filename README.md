## Circle

Circle first and foremost is a collection of libraries for full Kotlin Multiplatform development.
It is created for prototyping, demo and educational purpose.

Other than that, it provides a thin layer to make dev more easier to remember the APIs for
common usage such as:

- Storage
- Image Loading
- Navigation
- Multithreading

Not that **convenience** is the priority in this layer.

**This library is not intended for production usage**. Instead, use the transitive libraries
directly based on your needs.

## Usage

Set to shared `build.gradle`:

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implmenetation("io.github.esafirm.circle:circle:0.1.0")
            }
        }
    }
}
```

## License

MIT @ Esa Firman
