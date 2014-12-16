import com.typesafe.sbteclipse.core.EclipsePlugin
import sbt._
import xml._
import xml.Node
import xml.transform.RewriteRule

object SarosEclipse {
  lazy val sarosCruftTransformer = new EclipsePlugin.EclipseTransformerFactory[RewriteRule] {
    import scalaz.Scalaz._
    override def createTransformer(ref: ProjectRef, state: State): com.typesafe.sbteclipse.core.Validation[RewriteRule] = {
      val rule = new RewriteRule {

        override def transform(node: Node): Seq[Node] = node match {
          case projectDescription if (projectDescription.label == "projectDescription") =>
            Elem(projectDescription.prefix, "projectDescription", projectDescription.attributes, projectDescription.scope, projectDescription.child ++ filterResourcesXml: _*)
          case other => other
        }
      }
      rule.success
    }
  }

  lazy val filterResourcesXml = <filteredResources>
        <filter>
            <id>1372932729291</id>
            <name></name>
            <type>26</type>
            <matcher>
                <id>org.eclipse.ui.ide.orFilterMatcher</id>
                <arguments>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-target</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-logs</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-project/project</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-project/target</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-tools</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-.git</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-.gradle</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-.idea</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-.target</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-build</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-screenshots</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-eclipse-output</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-.settings</arguments>
                    </matcher>
                </arguments>
            </matcher>
        </filter>
        <filter>
            <id>1372932729292</id>
            <name></name>
            <type>6</type>
            <matcher>
                <id>org.eclipse.ui.ide.orFilterMatcher</id>
                <arguments>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-.cache</arguments>
                    </matcher>
                    <matcher>
                        <id>org.eclipse.ui.ide.multiFilter</id>
                        <arguments>1.0-projectRelativePath-matches-false-false-.project</arguments>
                    </matcher>
                    <matcher>
						<id>org.eclipse.ui.ide.multiFilter</id>
						<arguments>1.0-projectRelativePath-matches-false-false-*~</arguments>
					</matcher>
                </arguments>
            </matcher>
        </filter>
        <filter>
            <id>1372932729294</id>
            <name></name>
            <type>26</type>
            <matcher>
                <id>org.eclipse.ui.ide.multiFilter</id>
                <arguments>1.0-projectRelativePath-matches-false-false-.ensime_lucene</arguments>
            </matcher>
        </filter>
    </filteredResources>

}


